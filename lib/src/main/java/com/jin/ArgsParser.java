package com.jin;

import java.util.ArrayList;

public class ArgsParser {

    private static class ArgInfo {
        public ArgInfo(String argName, String argValue) {
            this.name = argName;
            this.value = argValue;
        }
        String name;
        String value;
    }

    private ArrayList<ArgInfo> _args = new ArrayList<ArgInfo>();

    public ArgsParser(String[] args) {
        argsToArgInfos(args, _args);
    }

    public String getArgByName(String name) {
        for (ArgInfo a : _args) {
            if (a.name.equalsIgnoreCase(name)) {
                return a.value;
            }
        }
        return null;
    }

    public ArrayList<String> getArgNames() {
        ArrayList<String> result = new ArrayList<String>();
        for (ArgInfo a : _args) {
            result.add(a.name);
        }
        return result;
    }

    private static void argsToArgInfos(String[] args, ArrayList<ArgInfo> result) {
        for (String arg : args) {
            if (!arg.startsWith("--")) {
                continue;
            }
            arg = arg.substring(2).trim();
            int pos = arg.indexOf('=');
            if (pos <= 0) {
                result.add(new ArgInfo(arg, null));
            } else {
                String argName = arg.substring(0, pos).trim();
                String argValue = arg.substring(pos+1).trim();
                result.add(new ArgInfo(argName, strip(argValue)));
            }
        }
    }

    // 去掉两端可能的双引号
    private static String strip(String argValue) {
        if (argValue.startsWith("\"") && argValue.endsWith("\"")) {
            int endIndex = argValue.length() - 1;
            if (endIndex == 1) {
                argValue = null;
            } else {
                argValue = argValue.substring(1, endIndex);
            }
        }
        return argValue;
    }
}