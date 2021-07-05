package com.jin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnnotatedParserTest {

    static class CmdArguments {

        @Argument("providerId")
        String providerId;

        @Argument("wmtscapUrl")
        String wmtscapUrl;

        @Argument("destPath")
        String destPath;

        @Argument(value = "debug", required = false)
        boolean debug;

    }

    private static void printUsage(String mainClassName) {
        System.out.println("产生接入WMTS服务的配置文件.");
        String cmd;
        if ((cmd = System.getProperty("GTCLOUD_INTERACTIVE_CMD")) == null) {
            System.out.format("用法: java %s 选项...%n", mainClassName);
        } else {
            System.out.format("用法: %s 选项...%n", cmd);
        }

        System.out.println("选项包括:");
        System.out.println("    --providerId=服务提供者的唯一标识, 如esri");
        System.out.println("    --destPath=配置文件生成后放在哪个目录下");
        System.out.println("    --wmtscapUrl=wmts元数据服务的访问URL");
        System.out.println("    [--dpi=96 | --dpi=90.7142857142857]");
        System.out.println("    [--debug]");
        System.out.println("用法举例:");
        if (cmd == null) {
            cmd = "java " + mainClassName;
        }

        System.out.format("%s --providerId=gttech" +
                " --destPath=/tmp/3rdwmts" +
                " --wmtscapUrl=http://localhost/maps/ogc/wmts/v1/全球影像/WMTSCapabilities.xml%n", cmd);
    }

    @Test
    public void testDataTypes() throws Exception {
        String[] args = {
                "--providerId=tianditu",
                "--wmtscapUrl=http://localhost/WMTSCapabilities.xml",
                "--destPath=C:\\Temp\\3rdwmts",
//                "--debug=true",
        };

        final String className = AnnotatedParserTest.class.getName();
        for (String arg : args) {
            if (arg.equals("--help") || arg.equals("-h")) {
                printUsage(className);
                System.exit(0);
            }
        }

        AnnotatedArgsParser parser = new AnnotatedArgsParser();
        CmdArguments arguments = parser.parse(CmdArguments.class, args);

        assertEquals("tianditu", arguments.providerId);
        assertEquals("http://localhost/WMTSCapabilities.xml", arguments.wmtscapUrl);
        assertEquals("C:\\Temp\\3rdwmts", arguments.destPath);
        assertFalse(arguments.debug);
    }

    @Test
    public void testMandatory() throws Exception {
        String[] args = {
                "--providerId",
                "--wmtscapUrl=http://localhost/WMTSCapabilities.xml",
                "--destPath=C:\\Temp\\3rdwmts",
        };
        AnnotatedArgsParser parser = new AnnotatedArgsParser();
        assertThrows(IllegalArgumentException.class, () -> {
            CmdArguments opt = parser.parse(CmdArguments.class, args);
        });
    }
}
