package com.valevich.materikemployee.util;


import android.content.Context;
import android.os.Environment;

import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.model.request.BulkUpdateModel;
import com.valevich.materikemployee.network.model.response.CatalogItem;
import com.valevich.materikemployee.network.model.response.FeatureItem;
import com.valevich.materikemployee.network.model.response.ProductItem;
import com.valevich.materikemployee.network.model.response.StockModel;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.StringRes;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observable;

@EBean
public class ExcelHelper {

    @StringRes(R.string.storage_unavailable)
    String storageUnavailableMessage;

    @StringRes(R.string.error_file_read)
    String readingErrorMessage;

    @StringRes(R.string.error_file_write)
    String writingErrorMessage;

    @StringRes(R.string.product_name)
    String productNameHeader;

    @StringRes(R.string.product_category)
    String productCategoryHeader;

    @StringRes(R.string.product_price)
    String productPriceHeader;

    @StringRes(R.string.product_articul)
    String productArticulHeader;

    @StringRes(R.string.product_image)
    String productImageHeader;

    @StringRes(R.string.product_amount)
    String productAmountHeader;

    @StringRes(R.string.product_metrics)
    String productMetricsHeader;

    @StringRes(R.string.product_stock_address)
    String productStockHeader;

    @StringRes(R.string.category_name)
    String categoryNameHeader;

    @StringRes(R.string.category_image)
    String categoryImageHeader;

    @StringRes(R.string.stock_address)
    String stockAddressHeader;

    @StringRes(R.string.feature_name)
    String featureNameHeader;

    @StringRes(R.string.feature_value)
    String featureValueHeader;

    @StringRes(R.string.id)
    String idHeader;

    @StringRes(R.string.feature_product)
    String featureProductHeader;

    @StringRes(R.string.sheet_title_products)
    String productsTitle;

    @StringRes(R.string.sheet_title_categories)
    String categoriesTitle;

    @StringRes(R.string.sheet_title_features)
    String featuresTitle;

    @StringRes(R.string.sheet_title_stocks)
    String stocksTitle;

    @StringRes(R.string.stats_storage)
    String statsDirName;

    @RootContext
    Context context;

    public Observable<BulkUpdateModel> importCatalog(String filename) {

        return Observable.defer(() -> {

            if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
                return Observable.error(new RuntimeException(storageUnavailableMessage));
            }

            try {
                File file = new File(filename);
                FileInputStream fileInputStream = new FileInputStream(file);
                POIFSFileSystem myFileSystem = new POIFSFileSystem(fileInputStream);
                HSSFWorkbook workbook = new HSSFWorkbook(myFileSystem);

                return Observable.just(new BulkUpdateModel(readCatalog(workbook),readStocks(workbook)));

            } catch (Exception e) {
                return Observable.error(new RuntimeException(readingErrorMessage));
            }

        });

    }

    public boolean exportCatalog(String fileName, List<StockModel> stocks, List<CatalogItem> catalog) {
        boolean success = false;
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            throw new RuntimeException(storageUnavailableMessage);
        }
        //New Workbook
        Workbook wb = new HSSFWorkbook();
        Map<ProductItem, String> products = new HashMap<>();
        Map<FeatureItem,String> features = new HashMap<>();
        for (CatalogItem catalogItem : catalog) {
            List<ProductItem> productItems = catalogItem.getProducts();
            if (productItems != null) {
                for (int i = 0; i < productItems.size(); i++) {
                    ProductItem productItem = productItems.get(i);
                    products.put(productItem, catalogItem.getName());
                    List<FeatureItem> featureItems = productItem.getFeatureList();
                    if(featureItems != null) {
                        for(FeatureItem featureItem:featureItems) {
                            features.put(featureItem,productItem.getArticul());
                        }
                    }
                }
            }
        }

        createProductsSheet(wb, products);
        createStockSheet(wb, stocks);
        createCategorySheet(wb,catalog);
        createFeatureSheet(wb,features);


        // Create a path where we will place our List of objects on external storage
        File appDir = new File(Environment.getExternalStorageDirectory(),statsDirName);
        if(appDir.mkdir() || appDir.isDirectory()) {
            File file = new File(appDir, fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                success = true;
            } catch (Exception e) {
                throw new RuntimeException(writingErrorMessage);
            } finally {
                try {
                    if (null != os)
                        os.close();
                } catch (Exception ex) {
                    success = false;
                }
            }
        } else success = false;

        return success;

    }

    private void createStockSheet(Workbook workbook, List<StockModel> stocks) {
        Sheet sheet = setUpHeader(workbook, stocksTitle, new ArrayList<>(Arrays.asList(stockAddressHeader,idHeader)));
        for (int i = 0; i < stocks.size(); i++) {
            StockModel stockModel = stocks.get(i);
            Row row = sheet.createRow(i + 1);
            Cell c1 =  row.createCell(0);
            c1.setCellValue(stockModel.getAddress());
            c1.setCellStyle(getUnLockedStyle(workbook));
            row.createCell(1).setCellValue(stockModel.getId());
        }
    }

    private void createProductsSheet(Workbook workbook, Map<ProductItem, String> products) {
        List<String> headers = new ArrayList<>(Arrays.asList(
                productNameHeader, productCategoryHeader, productPriceHeader, productArticulHeader,
                productImageHeader, productAmountHeader, productMetricsHeader, productStockHeader,idHeader
        ));
        Sheet sheet = setUpHeader(workbook, productsTitle, headers);

        for (int i = 0; i < products.size(); i++) {
            ProductItem productItem = (ProductItem) products.keySet().toArray()[i];
            Row row = sheet.createRow(i + 1);
            Cell c0 = row.createCell(0);
            c0.setCellValue(productItem.getName());
            c0.setCellStyle(getUnLockedStyle(workbook));
            Cell c1 = row.createCell(1);
            c1.setCellValue(products.get(productItem));
            c1.setCellStyle(getUnLockedStyle(workbook));
            Cell c2 = row.createCell(2);
            c2.setCellValue(productItem.getPrice());
            c2.setCellStyle(getUnLockedStyle(workbook));
            Cell c3 = row.createCell(3);
            c3.setCellValue(productItem.getArticul());
            c3.setCellStyle(getUnLockedStyle(workbook));
            Cell c4 = row.createCell(4);
            c4.setCellValue(productItem.getImageUrl());
            c4.setCellStyle(getUnLockedStyle(workbook));
            Cell c5 = row.createCell(5);
            c5.setCellValue(productItem.getStockAmount());
            c5.setCellStyle(getUnLockedStyle(workbook));
            Cell c6 = row.createCell(6);
            c6.setCellValue(productItem.getMetrics());
            c6.setCellStyle(getUnLockedStyle(workbook));
            Cell c7 = row.createCell(7);
            c7.setCellValue(productItem.getStockAddress());
            c7.setCellStyle(getUnLockedStyle(workbook));
            row.createCell(8).setCellValue(productItem.getId());
        }
    }

    private void createCategorySheet(Workbook workbook, List<CatalogItem> catalog) {
        List<String> headers = new ArrayList<>(Arrays.asList(categoryNameHeader,categoryImageHeader,idHeader));
        Sheet sheet = setUpHeader(workbook, categoriesTitle, headers);

        for (int i = 0; i < catalog.size(); i++) {
            CatalogItem catalogItem = catalog.get(i);
            Row row = sheet.createRow(i + 1);
            Cell c0 = row.createCell(0);
            c0.setCellValue(catalogItem.getName());
            c0.setCellStyle(getUnLockedStyle(workbook));
            Cell c1 = row.createCell(1);
            c1.setCellValue(catalogItem.getImageUrl());
            c1.setCellStyle(getUnLockedStyle(workbook));
            row.createCell(2).setCellValue(catalogItem.getId());
        }

    }

    private void createFeatureSheet(Workbook workbook, Map<FeatureItem, String> features) {
        List<String> headers = new ArrayList<>(Arrays.asList(featureNameHeader,featureValueHeader,featureProductHeader));
        Sheet sheet = setUpHeader(workbook, featuresTitle, headers);

        for (int i = 0; i < features.size(); i++) {
            FeatureItem featureItem = (FeatureItem) features.keySet().toArray()[i];
            Row row = sheet.createRow(i + 1);
            Cell c0 = row.createCell(0);
            c0.setCellValue(featureItem.getName());
            c0.setCellStyle(getUnLockedStyle(workbook));
            Cell c1 = row.createCell(1);
            c1.setCellValue(featureItem.getValue());
            c1.setCellStyle(getUnLockedStyle(workbook));
            Cell c2 = row.createCell(2);
            c2.setCellValue(features.get(featureItem));
            c2.setCellStyle(getUnLockedStyle(workbook));
        }

    }

    private Sheet setUpHeader(Workbook workbook, String title, List<String> headers) {
        Cell c;
        //Cell style for header row
        CellStyle cs = workbook.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //New Sheet
        Sheet sheet;
        sheet = workbook.createSheet(title);
        sheet.protectSheet("password");
        // Generate column headings
        Row row = sheet.createRow(0);

        for (int i = 0; i < headers.size(); i++) {
            c = row.createCell(i);
            c.setCellValue(headers.get(i));
            c.setCellStyle(cs);
            sheet.setColumnWidth(i,10000);
        }
        return sheet;
    }

    private List<CatalogItem> readCatalog(HSSFWorkbook workbook) {
        List<CatalogItem> catalogItems = new ArrayList<>();
        HSSFSheet categoriesSheet = workbook.getSheet(categoriesTitle);

        Iterator rowIterator = categoriesSheet.rowIterator();

        while (rowIterator.hasNext()) {
            HSSFRow row = (HSSFRow) rowIterator.next();
            if(row.getRowNum() != 0) {
                CatalogItem catalogItem = new CatalogItem();
                catalogItem.setName(row.getCell(0).getStringCellValue());
                catalogItem.setImageUrl(row.getCell(1).getStringCellValue());
                catalogItem.setId((int) row.getCell(2).getNumericCellValue());
                catalogItem.setProducts(readProducts(workbook,catalogItem.getName()));
                catalogItems.add(catalogItem);
            }
        }
        return catalogItems;
    }

    private List<ProductItem> readProducts(HSSFWorkbook workbook,String categoryName) {
        List<ProductItem> productItems = new ArrayList<>();
        HSSFSheet categoriesSheet = workbook.getSheet(productsTitle);

        Iterator rowIterator = categoriesSheet.rowIterator();

        while (rowIterator.hasNext()) {
            HSSFRow row = (HSSFRow) rowIterator.next();
            if(row.getRowNum() != 0) {
                ProductItem productItem = new ProductItem();
                String productCategoryName = row.getCell(1).getStringCellValue();
                if(productCategoryName.equals(categoryName)) {
                    productItem.setName(row.getCell(0).getStringCellValue());
                    productItem.setPrice(row.getCell(2).getNumericCellValue());
                    productItem.setArticul(row.getCell(3).getStringCellValue());
                    productItem.setImageUrl(row.getCell(4).getStringCellValue());
                    productItem.setStockAmount((int) row.getCell(5).getNumericCellValue());
                    productItem.setMetrics(row.getCell(6).getStringCellValue());
                    productItem.setStockAddress(row.getCell(7).getStringCellValue());
                    productItem.setId((int) row.getCell(8).getNumericCellValue());
                    productItem.setFeatureList(readFeatures(workbook,productItem.getArticul()));
                    productItems.add(productItem);
                }
            }
        }
        return productItems;
    }

    private List<FeatureItem> readFeatures(HSSFWorkbook workbook,String productArticul) {
        List<FeatureItem> featureItems = new ArrayList<>();
        HSSFSheet categoriesSheet = workbook.getSheet(featuresTitle);

        Iterator rowIterator = categoriesSheet.rowIterator();

        while (rowIterator.hasNext()) {
            HSSFRow row = (HSSFRow) rowIterator.next();
            if(row.getRowNum() != 0) {
                FeatureItem featureItem = new FeatureItem();
                String featureProductArticul = row.getCell(2).getStringCellValue();
                if(featureProductArticul.equals(productArticul)) {
                    featureItem.setName(row.getCell(0).getStringCellValue());
                    featureItem.setValue(row.getCell(1).getStringCellValue());
                    featureItems.add(featureItem);
                }
            }
        }
        return featureItems;
    }

    private List<StockModel> readStocks(HSSFWorkbook workbook) {
        List<StockModel> stockModels = new ArrayList<>();
        HSSFSheet categoriesSheet = workbook.getSheet(stocksTitle);

        Iterator rowIterator = categoriesSheet.rowIterator();

        while (rowIterator.hasNext()) {
            HSSFRow row = (HSSFRow) rowIterator.next();
            if(row.getRowNum() != 0) {
                StockModel stockModel = new StockModel();
                stockModel.setAddress(row.getCell(0).getStringCellValue());
                stockModel.setId((int) row.getCell(1).getNumericCellValue());
                stockModels.add(stockModel);
            }
        }
        return stockModels;
    }

    private CellStyle getUnLockedStyle(Workbook workbook) {
        CellStyle unlockedCellStyle = workbook.createCellStyle();
        unlockedCellStyle.setLocked(false);
        return unlockedCellStyle;
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }
}
