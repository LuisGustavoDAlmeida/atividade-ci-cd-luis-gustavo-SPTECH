package com.sptech.SistemasDistribuidos.Scraping;

import com.sptech.SistemasDistribuidos.Entities.CharacterData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CharacterScraping {

    public CharacterData scrape(String nomePersonagem) throws IOException {
        String nomeFormatado = nomePersonagem.replace(" ", "_");
        String url = "https://finalfantasy.fandom.com/wiki/" + nomeFormatado;
        Document doc;

        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .get();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao conectar ou buscar dados da URL: " + url, e);
        } finally {
            System.out.println("Conexão encerrada.");
        }

        return parseCharacterData(doc, nomePersonagem);
    }

    private CharacterData parseCharacterData(Document doc, String nomePadrao) {
        CharacterData data = new CharacterData();
        data.setNome(nomePadrao);

        Elements infoItems = doc.select(".pi-item.pi-data");


        for (Element item : infoItems) {
            String labelFromH3 = Optional.ofNullable(item.select(".pi-data-label").first())
                    .map(Element::text)
                    .map(s -> s.replace(":", "").trim())
                    .orElse("");

            if (labelFromH3.isEmpty()) {
                Element valueElement = item.select(".pi-data-value").first();
                if (valueElement != null) {
                    String valueHtml = valueElement.html();
                    if (valueHtml.contains("Alternate names:")) {
                        data.setApelidos(extractAlternateName(valueHtml));
                        break;
                    }
                }
            }
        }


        for (Element item : infoItems) {
            String label = Optional.ofNullable(item.select(".pi-data-label").first())
                    .map(Element::text)
                    .map(s -> s.replace(":", "").trim())
                    .orElse("");

            String value = "";
            if (label.toLowerCase().equals("japanese voice actor") || label.toLowerCase().equals("english voice actor")) {
                value = Optional.ofNullable(item.select(".pi-data-value").first())
                        .map(Element::html)
                        .orElse("");
            } else {
                value = Optional.ofNullable(item.select(".pi-data-value").first())
                        .map(Element::text)
                        .orElse("");
            }

            value = cleanValueBasic(value);

            if (label.isEmpty() || value.isEmpty()) {

                continue;
            }

            switch (label.toLowerCase()) {
                case "home":
                    data.setLar(value);
                    break;
                case "age":
                    value = removeParenthesizedPhrases(value, "born");
                    data.getIdade().add(value.trim());
                    break;
                case "affiliation":
                    data.setAfiliacao(value);
                    break;
                case "occupation":
                    List<String> cargosList = splitByCommaOrLineBreak(value);
                    cargosList.forEach(data.getCargos()::add);
                    break;
                case "loves":
                    data.setHobby(value.trim());
                    break;
                case "race":
                    data.setRaca(value);
                    break;
                case "gender":
                    data.setGenero(value);
                    break;
                case "height":
                    data.setAltura(value.trim());
                    break;
                case "hair color":
                    data.setCorCabelo(value);
                    break;
                case "eye color":
                    data.setCorOlhos(value);
                    break;
                case "type":
                    data.setTipoJogador(removeParentheses(value).trim());
                    break;
                case "weapon":
                    List<String> finalArmas = new ArrayList<>();
                    String[] parts = value.split("Signature weapons:");
                    if (parts.length > 0) {
                        splitByCommaOrSemiColon(parts[0].trim()).forEach(finalArmas::add);
                        if (parts.length > 1) {
                            splitByCommaOrSemiColon(parts[1].trim()).forEach(finalArmas::add);
                        }
                    }
                    data.getArmas().addAll(finalArmas);
                    break;
                case "designer":
                    data.setDesigner(value);
                    break;
                case "motion capture":
                    data.setCapturaDeMovimento(value);
                    break;
                case "japanese voice actor":
                    List<String> japActors = splitVoiceActors(value);
                    japActors.forEach(data.getDubladorJap()::add);
                    break;
                case "english voice actor":
                    List<String> engActors = splitVoiceActors(value);
                    engActors.forEach(data.getDubladorEng()::add);
                    break;
                case "party member gameplay":
                    Elements links = item.select(".pi-data-value li a");
                    for (Element link : links) {
                        String appearanceName = link.text().trim();
                        if (!appearanceName.isEmpty() && !appearanceName.equalsIgnoreCase("party member info")) {
                            data.getAparicoes().add(appearanceName);
                        }
                    }
                    break;
                case "enemy appearances":
                    // Verifica se o valor é apenas "Boss" ou se há outros valores
                    if (!value.equalsIgnoreCase("Boss")) {
                        // Se houver mais do que só "Boss", ou se não for "Boss"
                        Elements enemyLinks = item.select(".pi-data-value li a");
                        if (enemyLinks.isEmpty()) { // Se não tiver links, tenta splitar o texto
                            splitAppearances(value).forEach(data.getAparicoes()::add);
                        } else { // Se tiver links, usa o texto dos links
                            for (Element link : enemyLinks) {
                                String appearanceName = link.text().trim();
                                if (!appearanceName.isEmpty() && !appearanceName.equalsIgnoreCase("Boss")) {
                                    data.getAparicoes().add(appearanceName);
                                }
                            }
                        }
                    }
                    break;
                case "other appearances":
                    Elements otherLinks = item.select(".pi-data-value li a");
                    for (Element link : otherLinks) {
                        String appearanceName = link.text().trim();
                        if (!appearanceName.isEmpty() && !appearanceName.equalsIgnoreCase("All other appearances")) {
                            data.getAparicoes().add(appearanceName);
                        }
                    }
                    break;
                default:
                    System.out.println("Rótulo não mapeado: '" + label + "' com valor: '" + value + "'");
                    break;
            }
        }
        return data;
    }

    private String extractAlternateName(String valueHtml) {

        String fullText = valueHtml.replace("<br>", "\n").replace("<p>", "\n").replace("</p>", "").trim();
        fullText = Jsoup.parse(fullText).text(); // Agora pega o texto limpo, com '\n' como separador

        int startIndex = fullText.indexOf("Alternate names:");
        if (startIndex == -1) {
            return "";
        }
        String afterPrefix = fullText.substring(startIndex + "Alternate names:".length()).trim();

        int jpTemplateStart = afterPrefix.indexOf(" (");
        if (jpTemplateStart != -1) {
            int jpTemplateEnd = afterPrefix.indexOf("?)", jpTemplateStart);
            if (jpTemplateEnd != -1) {
                String tempAfterJp = afterPrefix.substring(jpTemplateEnd + 2).trim();
                if (!tempAfterJp.isEmpty()) {
                    int nextParen = tempAfterJp.indexOf(" (");
                    if (nextParen != -1) {
                        return tempAfterJp.substring(0, nextParen).trim();
                    } else {
                        return tempAfterJp.trim();
                    }
                }
            }
        }
        return afterPrefix.trim();
    }


    private String cleanValueBasic(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        // Remove referências como [1] ou [note 1]
        StringBuilder sbNoReferences = new StringBuilder();
        boolean inReference = false;
        for (char c : text.toCharArray()) {
            if (c == '[') {
                inReference = true;
            } else if (c == ']' && inReference) {
                inReference = false;
            } else if (!inReference) {
                sbNoReferences.append(c);
            }
        }
        text = sbNoReferences.toString().trim();

        text = text.replace("\n", " ").replace("\r", " ");
        StringBuilder sb = new StringBuilder();
        boolean lastWasSpace = false;
        for (char c : text.toCharArray()) {
            if (Character.isWhitespace(c)) {
                if (!lastWasSpace) {
                    sb.append(' ');
                    lastWasSpace = true;
                }
            } else {
                sb.append(c);
                lastWasSpace = false;
            }
        }
        return sb.toString().trim();
    }

    private String removeParentheses(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        StringBuilder sb = new StringBuilder();
        int parenLevel = 0;
        for (char c : text.toCharArray()) {
            if (c == '(') {
                parenLevel++;
            } else if (c == ')') {
                if (parenLevel > 0) {
                    parenLevel--;
                }
            } else if (parenLevel == 0) {
                sb.append(c);
            }
        }
        return sb.toString().trim();
    }

    private String removeParenthesizedPhrases(String text, String keyword) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < text.length()) {
            char c = text.charAt(i);
            if (c == '(') {
                int startParen = i;
                int endParen = -1;
                int balance = 0;
                for (int j = i; j < text.length(); j++) {
                    if (text.charAt(j) == '(') {
                        balance++;
                    } else if (text.charAt(j) == ')') {
                        balance--;
                    }
                    if (balance == 0) {
                        endParen = j;
                        break;
                    }
                }

                if (endParen != -1) {
                    String content = text.substring(startParen + 1, endParen);
                    if (content.trim().startsWith(keyword)) {
                        i = endParen + 1;
                        continue;
                    } else {
                        result.append(text.substring(startParen, endParen + 1));
                        i = endParen + 1;
                        continue;
                    }
                } else {
                    result.append(text.substring(i));
                    break;
                }
            } else {
                result.append(c);
            }
            i++;
        }
        return result.toString().trim();
    }

    private List<String> splitByCommaOrLineBreak(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        String temp = text.replace("\n", ",").replace("\r", ",");
        temp = cleanValueBasic(temp);
        temp = temp.replace("• ", "").trim();

        return Arrays.stream(temp.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private List<String> splitByCommaOrSemiColon(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        String temp = text.replace(";", ",");
        return Arrays.stream(temp.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private List<String> splitVoiceActors(String htmlText) {
        if (htmlText == null || htmlText.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> actors = new ArrayList<>();
        String cleanedHtml = htmlText.replace("<sup>", "").replace("</sup>", "")
                .replace("<br>", "|||").trim(); // Usar um marcador improvável

        String[] parts = cleanedHtml.split("\\|\\|\\|");

        for (String part : parts) {
            String actorName = Jsoup.parse(part).text().trim(); // Limpa qualquer HTML restante
            if (!actorName.isEmpty()) {
                actors.add(actorName);
            }
        }
        return actors.stream().distinct().collect(Collectors.toList());
    }

    private List<String> splitAppearances(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        String temp = text.replace(" • ", ",")
                .replace("\n", ",")
                .replace("\r", ",")
                .replace("All other appearances", "");

        return Arrays.stream(temp.split("[, ]+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }
}