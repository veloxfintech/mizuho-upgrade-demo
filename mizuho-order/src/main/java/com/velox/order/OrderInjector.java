package com.velox.order;

import com.aralis.df.cache.CachePublisher;
import com.aralis.lists.Pair;
import com.aralis.tools.util.CSVToObjHelper;
import com.caelo.meta.TableDescriptor;
import com.velox.order.api.Order;
import com.velox.order.api.OrderSchemaDescriptor;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

public class OrderInjector {
    public OrderInjector(CachePublisher<Order, String> orderPub, String filename) {
        String orders = readFile(filename);
        Pair<TableDescriptor, List<?>> orderList =
          CSVToObjHelper.convert(OrderSchemaDescriptor.SchemaDescriptor, "Order", orders);
        orderPub.publish((List<Order>) orderList.getSecond());
    }

    private String readFile(String path) {
        try {
            InputStreamReader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(path), Charset.defaultCharset());
            java.util.Scanner s = new java.util.Scanner(reader).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (Exception e) {
            return null;
        }
    }
}
