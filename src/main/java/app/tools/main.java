package app.tools;

import com.fasterxml.jackson.databind.ObjectMapper;

public class main {

    public static void main(String[] args)  {


        tools tools = new tools();
        String log = null;

        /*
        String qrcode = null;

        String json = null;


        File file = new File("D:/Datamon/TRABAJOEXTERNO_1.pdf");
        System.out.println("Existe: " + file.exists());
        System.out.println("É arquivo: " + file.isFile());
        System.out.println("Absoluto: " + file.getAbsolutePath());

        String jsonContent = "[\n" +
                "  { \"realpath\": \"D:/Datamon/Doc_1.pdf\" },\n" +
                "  { \"realpath\": \"D:/Datamon/Doc_2.pdf\" },\n" +
                "  { \"realpath\": \"D:/Datamon/TRABAJOEXTERNO_1.pdf\" }\n" +
                "]";

        try {
            JsonNode rootNode = new ObjectMapper().readTree(jsonContent);
            System.out.println("JSON parseado com sucesso. Total de arquivos: " + rootNode.size());
        } catch (Exception e) {
            System.err.println("ERRO ao fazer parse do JSON: " + e.getMessage());
            e.printStackTrace();
        }

        String outputPath = "D:/Datamon/pdf_agrupado.pdf";


        log =  tools.merge(jsonContent, outputPath);
        System.out.println(log);

        */

        //qrcode = tools.qrcodeCM(3,"https://www.google.com","D:\\Projects\\Java\\AppTool\\qrcode.png");


        /*
        hash = tools.hashAt("D:\\Projects\\Java\\AppTool\\resources\\privateKey",
                "2012-04-04;2012-04-04T18:51:34;FAC 0/15474;501.96;");

        System.out.println("------------------HASH---------------------");
        System.out.println(hash);
        System.out.println("------------------HASH---------------------");
        */

/*
        String data = "3ª Câmara de Direito Público Pauta de Julgamentos Torno público que, de acordo com o artigo 934 do Código de Processo Civil c/c art. 142-L do Regimento Interno do Tribunal de Justiça de Santa Catarina, na Sessão Virtual do dia 06 de agosto de 2024, terça-feira, às 09h00min, serão julgados os seguintes processos: Apelação / Remessa Necessária Nº 5028387-94.2020.8.24.0033/SC (Pauta: 81) RELATORA: Desembargadora BETTINA MARIA MARESCH DE MOURA APELANTE: ESTADO DE SANTA CATARINA (INTERESSADO) PROCURADOR(A): MARCIO LUIZ FOGACA VICARI APELADO: GONCALVES & TORTOLA S/A - EM RECUPERACAO JUDICIAL (IMPETRANTE) ADVOGADO(A): ALAN ROGÉRIO MINCACHE (OAB PR031976) MP: MINISTÉRIO PÚBLICO DO ESTADO DE SANTA CATARINA (MP) INTERESSADO: GERENTE REGIONAL - ESTADO DE SANTA CATARINA - ITAJAÍ (IMPETRADO) Publique-se e Registre-se.Florianópolis, 19 de julho de 2024. Desembargador SANDRO JOSE NEIS Presidente";

        String ret = tools.BuscarDatas(data);

        System.out.println(ret);
        */


            String json = "{\n" +
                    "    \"Title\": \"Teste\",\n" +
                    "    \"Subject\": \"teste\",\n" +
                    "    \"HTMLText\": \"<h1>Hello Word</h1>\",\n" +
                    "    \"EmailTo\": \"jrkaibro@gmail.com\",\n" +
                    "    \"ReplyTo\": \"\",\n" +
                    "    \"ReplyFrom\": \"\",\n" +
                    "    \"CC\": [\n" +
                    "        {\n" +
                    "            \"CC\": \"jrkaibro@gmail.com\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"CCO\": [\n" +
                    "        {\n" +
                    "            \"CCO\": \"jrkaibro@gmail.com\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"Config\": {\n" +
                    "        \"User\": \"user\",\n" +
                    "        \"Password\": \"password\",\n" +
                    "        \"Autentication\": true,\n" +
                    "        \"Security\": true,\n" +
                    "        \"Port\": 587,\n" +
                    "        \"Email\": \"email@domain.com\",\n" +
                    "        \"Smtp\": \"smtp.com\"\n" +
                    "    },\n" +
                    "    \"Attached\": [\n" +
                    "        {\n" +
                    "            \"Path\": \"D:\\\\TEMP\\\\GuiaRemessa_FULL.pdf\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"Path\": \"D:\\\\TEMP\\\\GuiaRemessa_FULL.pdf\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";


            String Base64 = "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";

            log = tools.Base64ToFile(Base64,"c:\\temp\\PackingList_320_299045_20250717.xlsx");
            System.out.println(log);


    }
}
