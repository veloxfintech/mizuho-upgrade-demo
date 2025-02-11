package com.velox.position;

import com.aralis.df.cache.CachePublisher;
import com.aralis.lists.Pair;
import com.aralis.tools.util.CSVToObjHelper;
import com.caelo.meta.TableDescriptor;
import com.velox.position.api.Position;
import com.velox.position.api.PositionSchemaDescriptor;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

public class PositionInjector {
    public PositionInjector(CachePublisher<Position, String> positionPub, String filename) {
        String orders = readFile(filename);
        Pair<TableDescriptor, List<?>> orderList =
          CSVToObjHelper.convert(PositionSchemaDescriptor.SchemaDescriptor, "Position", orders);
        positionPub.publish((List<Position>) orderList.getSecond());
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
