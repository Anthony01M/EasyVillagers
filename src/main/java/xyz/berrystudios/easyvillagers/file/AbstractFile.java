package xyz.berrystudios.easyvillagers.file;

import xyz.berrystudios.easyvillagers.util.misc.YamlLoader;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractFile {
    public static <T extends AbstractFile> T load(final String pathPrefix, final T instance, final YamlLoader loader) {
        Field[] fields = instance.getClass().getFields();
        for (Field field : fields) {
            Load annotation = field.getAnnotation(Load.class);
            if (annotation == null) continue;
            String path = pathPrefix == null ? annotation.path() : pathPrefix + "." + annotation.path();
            Object value = instance.getValue(path, loader, field.getType());
            if (value == null) continue;
            if (field.getType() == Set.class && value instanceof List) value = new HashSet<>((List<?>) value);
            try {
                field.set(instance, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public abstract Object getValue(String path, YamlLoader loader, Class<?> type);
}
