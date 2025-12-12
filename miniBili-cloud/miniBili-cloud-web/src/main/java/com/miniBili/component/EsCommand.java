package com.miniBili.component;

public  class EsCommand {

    public static final String COMMA = "{\n" +
            "  \"analysis\": {\n" +
            "    \"analyzer\": {\n" +
            "      \"comma\": {\n" +
            "        \"type\": \"pattern\",\n" +
            "        \"pattern\": \",\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public static final String CREATE_INDEX = "{\n" +
            "  \"properties\": {\n" +
            "    \"videoId\": {\n" +
            "      \"type\": \"keyword\",\n" +
            "      \"index\": false\n" +
            "    },\n" +
            "    \"userId\": {\n" +
            "      \"type\": \"keyword\",\n" +
            "      \"index\": false\n" +
            "    },\n" +
            "    \"videoCover\": {\n" +
            "      \"type\": \"keyword\",\n" +
            "      \"index\": false\n" +
            "    },\n" +
            "    \"videoName\": {\n" +
            "      \"type\": \"text\",\n" +
            "      \"analyzer\": \"ik_max_word\"\n" +
            "    },\n" +
            "    \"tags\": {\n" +
            "      \"type\": \"text\",\n" +
            "      \"analyzer\": \"comma\"\n" +
            "    },\n" +
            "    \"playCount\": {\n" +
            "      \"type\": \"integer\",\n" +
            "      \"index\": false\n" +
            "    },\n" +
            "    \"danmuCount\": {\n" +
            "      \"type\": \"integer\",\n" +
            "      \"index\": false\n" +
            "    },\n" +
            "    \"collectCount\": {\n" +
            "      \"type\": \"integer\",\n" +
            "      \"index\": false\n" +
            "    },\n" +
            "    \"createTime\": {\n" +
            "      \"type\": \"date\",\n" +
            "      \"format\": \"yyyy-MM-dd HH:mm:ss\",\n" +
            "      \"index\": false\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
