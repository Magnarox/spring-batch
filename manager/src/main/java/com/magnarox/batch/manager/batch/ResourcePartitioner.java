package com.magnarox.batch.manager.batch;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResourcePartitioner implements Partitioner {

    private Resource[] resources;

    public ResourcePartitioner setResources(Resource[] resources) {
        this.resources = resources;
        return this;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        final Map<String, ExecutionContext> map = new HashMap<>(gridSize);
        final String keyName = "filePath";
        final String partitionKey = "part_";

        try {
            int i = 0;
            for (Resource resource : this.resources) {
                final ExecutionContext context = new ExecutionContext();

                context.putString(keyName, resource.getFile().getAbsolutePath());
                map.put(partitionKey + i, context);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }
}
