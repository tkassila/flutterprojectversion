package com.metait.flutterprojectversion;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.NestedTableColumnHeader;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.control.skin.TableViewSkin;
import javafx.util.Callback;

import javax.swing.plaf.synth.Region;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class GUIUtils {
    /*
    private static Method columnToFitMethod;
    static {
        try {
            columnToFitMethod = TableViewSkin.class.getDeclaredMethod("resizeColumnToFitContent", TableColumn.class, int.class);
            columnToFitMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void autoFitTable(TableView tableView) {
        tableView.getItems().addListener(new ListChangeListener<Object>() {
            @Override
            public void onChanged(Change<?> c) {
                for (Object column : tableView.getColumns()) {
                    try {
                        columnToFitMethod.invoke(tableView.getSkin(), column, -1);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
     */


    /**
     * Auto-sizes table view columns to fit its contents.
     *
     * @param col      The column to resize.
     * @param minWidth Minimum desired width of text for this column. Use -1 for no minimum
     *                 width.
     * @param maxWidth Maximum desired width of text for this column. Use -1 for no maximum
     *                 width.
     * @param maxRows  Maximum number of rows to examine for auto-resizing. Use -1
     *                 for all rows.
     * @note This is not a column resize policy and does not prevent manual
     * resizing after this method has been called.
     */
    public static void autoSizeTableViewColumn(TableColumn<?, ?> column, int minWidth, int maxWidth, int maxRows) {
        TableView<?> tableView = column.getTableView();
        TableViewSkin<?> skin = (TableViewSkin<?>) tableView.getSkin();

        if (skin == null) {
            System.err.println(" tableView skin is null.");
            return;
        }

        /*
        TableHeaderRow headerRow = skin.getTableHeaderRow();
        NestedTableColumnHeader rootHeader = headerRow.getRootHeader();
        for (Node node : rootHeader.getChildren())
        {
            if (node instanceof TableColumnHeader)
            {
                TableColumnHeader columnHeader = (TableColumnHeader) node;
                if(columnHeader.getTableColumn().equals(column))
                {
                    autoSizeTableViewColumn(columnHeader, minWidth, maxWidth, maxRows);
                }
            }
        }
         */
    }

    /**
     * Auto-sizes a table view column to fit its contents.
     *
     * @param col      The column to resize.
     * @param minWidth Minimum desired width of text for this column. Use -1 for no minimum
     *                 width.
     * @param maxWidth Maximum desired width of text for this column. Use -1 for no maximum
     *                 width.
     * @param maxRows  Maximum number of rows to examine for auto-resizing. Use -1
     *                 for all rows.
     * @note This is not a column resize policy and does not prevent manual
     * resizing after this method has been called.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void autoSizeTableViewColumn(TableColumnHeader header, int minWidth, int maxWidth, int maxRows) {
        /*
        TableColumn<?, ?> col = header.getTableColumn();
        if(col != null)
        {
            List<?> items = col.getTableView().getItems();
            if (items == null || items.isEmpty())
                return;

            Callback cellFactory = col.getCellFactory();
            if (cellFactory == null)
                return;

            TableCell cell = (TableCell) cellFactory.call(col);
            if (cell == null)
                return;

            // set this property to tell the TableCell we want to know its actual
            // preferred width, not the width of the associated TableColumn
            cell.getProperties().put("deferToParentPrefWidth", Boolean.TRUE);

            // determine cell padding
            double padding = 10;
            Node n = cell.getSkin() == null ? null : cell.getSkin().getNode();
            if (n instanceof Region)
            {
                Region r = (Region) n;
                padding = r.getInsets().getLeft() + r.getInsets().getRight();
            }

            int rows = maxRows == -1 ? items.size() : Math.min(items.size(), maxRows);

            double desiredWidth = 0;

            // Check header
            Label headerLabel = (Label) header.lookup(".label");
            String headerText = headerLabel.getText();
            if (!headerLabel.getContentDisplay().equals(ContentDisplay.GRAPHIC_ONLY) && headerText != null)
            {
                Text text = new Text(headerLabel.getText());
                text.setFont(headerLabel.getFont());
                desiredWidth += text.getLayoutBounds().getWidth() + headerLabel.getLabelPadding().getLeft() + headerLabel.getLabelPadding().getRight();
            }

            Node headerGraphic = headerLabel.getGraphic();
            if((headerLabel.getContentDisplay().equals(ContentDisplay.LEFT) || headerLabel.getContentDisplay().equals(ContentDisplay.RIGHT)) && headerGraphic != null)
            {
                desiredWidth += headerGraphic.getLayoutBounds().getWidth();
            }

            // Handle minimum width calculations
            // Use a "w" because it is typically the widest character
            Text minText = new Text(StringUtils.repeat("W", Math.min(0, minWidth)));
            minText.setFont(headerLabel.getFont());

            // Check rows
            double minPxWidth = 0;
            for (int row = 0; row < rows; row++)
            {
                cell.updateTableColumn(col);
                cell.updateTableView(col.getTableView());
                cell.updateIndex(row);

                // Handle minimum width calculations
                // Just do this once
                if(row == 0)
                {
                    String oldText = cell.getText();
                    // Use a "w" because it is typically the widest character
                    cell.setText(StringUtils.repeat("W", Math.max(0, minWidth)));

                    header.getChildren().add(cell);
                    cell.impl_processCSS(false);
                    minPxWidth = cell.prefWidth(-1);
                    header.getChildren().remove(cell);

                    cell.setText(oldText);
                }

                if ((cell.getText() != null && !cell.getText().isEmpty()) || cell.getGraphic() != null)
                {
                    header.getChildren().add(cell);
                    cell.impl_processCSS(false);
                    desiredWidth = Math.max(desiredWidth, cell.prefWidth(-1));
                    desiredWidth = Math.max(desiredWidth, minPxWidth);
                    header.getChildren().remove(cell);
                }
            }

            desiredWidth = desiredWidth + padding;

            if(maxWidth > 0)
            {
                desiredWidth = Math.min(maxWidth, desiredWidth);
            }

            col.impl_setWidth(desiredWidth);
        }
       */
    }
}
