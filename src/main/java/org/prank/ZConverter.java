package org.prank;

import stanhebben.zenscript.ZenParsedFile;
import stanhebben.zenscript.ZenTokener;
import stanhebben.zenscript.statements.Statement;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ZConverter {
    private static String UTF8 = "UTF-8";

    public static String convert(String input) {
        try {
            Path path = Paths.get(input);
            File src = path.toFile();
            if (!src.isDirectory()) return input + " is not a folder!";

            File[] files = src.listFiles();
            if (files == null) return "No ZS files found";

            for (File file : files) {
                if (!file.getName().endsWith(".zs")) continue;
                String zsCode = readAllText(file);
                String luaCode = convertScript(zsCode);
                writeAllText(toLua(file.getAbsolutePath()), luaCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return input;
        }

        try {
            return convertScript(input);
        } catch (IOException e) {
            e.printStackTrace();
            return input;
        }
    }

    private static void writeAllText(String path, String text) throws IOException {
        Files.write(Paths.get(path), text.getBytes(UTF8));
    }

    private static String toLua(String path) {
        return path.substring(0, path.lastIndexOf('.')) + ".lua";
    }

    private static String readAllText(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), UTF8);
    }

    private static String convertScript(String input) throws IOException {
        ZenTokener zenTokener = new ZenTokener(input, new CompileEnv());
        ZenParsedFile zenParsedFile = new ZenParsedFile("text", "classname", zenTokener, new GlobalEnv());

        StringBuilder sb = new StringBuilder("-- Autogenerated Lua script:").append(Statement.nl);
        for (Statement statement : zenParsedFile.getStatements()) {
            sb.append(statement.toLua());
        }
        return sb.toString();
    }

    public static String join(List<?> list) {
        return join(list, ", ");
    }

    public static String join(List<?> list, String delimiter) {
        return list.stream().map(Object::toString).collect(Collectors.joining(delimiter));
    }
}
