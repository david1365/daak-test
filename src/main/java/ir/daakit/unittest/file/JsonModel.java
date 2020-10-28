package ir.daakit.unittest.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public class JsonModel {
    private static final String suffix = "json";

    private static Gson gson;

    static {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
    }

    private static class ListParameterizedType implements ParameterizedType {

        private Type type;

        private ListParameterizedType(Type type) {
            this.type = type;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{type};
        }

        @Override
        public Type getRawType() {
            return ArrayList.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }

        // implement equals method too! (as per javadoc)
    }

    public static String fromJson(String path) throws IOException {
        path = String.format("%s.%s", path.replace(".", "/"), suffix);

        File file = ResourceUtils.getFile(String.format("%s:%s", "classpath", path));

        return new String(Files.readAllBytes(file.toPath()));
    }

    public static <M> M getModel(String path, Class<M> mClass, Type... typeArguments) throws IOException {
        String jsonContent = fromJson(path);

        Type type = TypeToken.getParameterized(mClass, typeArguments).getType();

        return gson.fromJson(jsonContent, type);
    }

    public static <M> M getModel(String path, Class<M> mClass) throws IOException {
        String jsonContent = fromJson(path);

        return gson.fromJson(jsonContent, mClass);
    }
}
