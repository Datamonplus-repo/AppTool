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

        log = tools.email(json);
        System.out.println(log);


    }
}
