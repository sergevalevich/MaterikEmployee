package com.valevich.materikemployee.util;


import android.content.Context;
import android.os.Environment;

import com.valevich.materikemployee.R;
import com.valevich.materikemployee.network.model.response.CatalogItem;
import com.valevich.materikemployee.network.model.response.FeatureItem;
import com.valevich.materikemployee.network.model.response.ProductItem;
import com.valevich.materikemployee.network.model.response.StockModel;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.StringRes;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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

    public Observable<List<CatalogItem>> readExcelFile(String filename) {

//        Observable.defer();
//
//        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
//            return Observable.error(new RuntimeException(storageUnavailableMessage));
//        }
//
//        try {
//            File file = new File(filename);
//            FileInputStream fileInputStream = new FileInputStream(file);
//
//            // Create a POIFSFileSystem object
//            POIFSFileSystem myFileSystem = new POIFSFileSystem(fileInputStream);
//
//            // Create a workbook using the File System
//            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
//
//            // Get the first sheet from workbook
//            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
//
//            /** We now need something to iterate through the cells.**/
//            Iterator rowIter = mySheet.rowIterator();
//
//            while (rowIter.hasNext()) {
//                HSSFRow myRow = (HSSFRow) rowIter.next();
//                Iterator cellIterator = myRow.cellIterator();
//                while (cellIterator.hasNext()) {
//                    HSSFCell myCell = (HSSFCell) cellIterator.next();
//                    Timber.d("Cell Value: %s", myCell.toString());
//                }
//            }
//        } catch (Exception e) {
//            return Observable.error(new RuntimeException(readingErrorMessage));
//        }
//
//        return Observable.just(true);
        return null;

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
        Sheet sheet = setUpHeader(workbook, stocksTitle, new ArrayList<>(Collections.singletonList(stockAddressHeader)));
        for (int i = 0; i < stocks.size(); i++) {
            StockModel stockModel = stocks.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(stockModel.getAddress());
        }
    }

    private void createProductsSheet(Workbook workbook, Map<ProductItem, String> products) {
        List<String> headers = new ArrayList<>(Arrays.asList(
                productNameHeader, productCategoryHeader, productPriceHeader, productArticulHeader,
                productImageHeader, productAmountHeader, productMetricsHeader, productStockHeader
        ));
        Sheet sheet = setUpHeader(workbook, productsTitle, headers);

        for (int i = 0; i < products.size(); i++) {
            ProductItem productItem = (ProductItem) products.keySet().toArray()[i];
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(productItem.getName());
            row.createCell(1).setCellValue(products.get(productItem));
            row.createCell(2).setCellValue(productItem.getPrice());
            row.createCell(3).setCellValue(productItem.getArticul());
            row.createCell(4).setCellValue(productItem.getImageUrl());
            row.createCell(5).setCellValue(productItem.getStockAmount());
            row.createCell(6).setCellValue(productItem.getMetrics());
            row.createCell(7).setCellValue(productItem.getStockAddress());
        }
    }

    private void createCategorySheet(Workbook workbook, List<CatalogItem> catalog) {
        List<String> headers = new ArrayList<>(Arrays.asList(categoryNameHeader,categoryImageHeader));
        Sheet sheet = setUpHeader(workbook, categoriesTitle, headers);

        for (int i = 0; i < catalog.size(); i++) {
            CatalogItem catalogItem = catalog.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(catalogItem.getName());
            row.createCell(1).setCellValue(catalogItem.getImageUrl());
        }

    }

    private void createFeatureSheet(Workbook workbook, Map<FeatureItem, String> features) {
        List<String> headers = new ArrayList<>(Arrays.asList(featureNameHeader,featureValueHeader,featureProductHeader));
        Sheet sheet = setUpHeader(workbook, featuresTitle, headers);

        for (int i = 0; i < features.size(); i++) {
            FeatureItem featureItem = (FeatureItem) features.keySet().toArray()[i];
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(featureItem.getName());
            row.createCell(1).setCellValue(featureItem.getValue());
            row.createCell(2).setCellValue(features.get(featureItem));
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

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }
}
