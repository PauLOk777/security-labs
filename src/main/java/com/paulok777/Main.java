package com.paulok777;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        singleXor();
        repeatingXor();
    }

    public static void singleXor() {
        String text = "7958401743454e1756174552475256435e59501a5c524e176f786517545e475f5245191772195019175e4317445f58425b531743565c521756174443455e595017d5b7ab5f525b5b58174058455b53d5b7aa175659531b17505e41525917435f52175c524e175e4417d5b7ab5c524ed5b7aa1b174f584517435f5217515e454443175b524343524517d5b7ab5fd5b7aa17405e435f17d5b7ab5cd5b7aa1b17435f5259174f584517d5b7ab52d5b7aa17405e435f17d5b7ab52d5b7aa1b17435f525917d5b7ab5bd5b7aa17405e435f17d5b7ab4ed5b7aa1b1756595317435f5259174f58451759524f4317545f564517d5b7ab5bd5b7aa17405e435f17d5b7ab5cd5b7aa175650565e591b17435f525917d5b7ab58d5b7aa17405e435f17d5b7ab52d5b7aa1756595317445817585919176e5842175a564e17424452175659175e5953524f1758511754585e59545e53525954521b177f565a5a5e595017535e4443565954521b177c56445e445c5e17524f565a5e5956435e58591b17444356435e44435e54565b17435244434417584517405f564352415245175a52435f5853174e5842175152525b174058425b5317445f584017435f52175552444317455244425b4319";
        String asciiText = convertHex(text, StandardCharsets.UTF_8);
        SingleByteXorCipher singleByteXorCipher = new SingleByteXorCipher();
        Map<Integer, String> texts = singleByteXorCipher.hackSingleXorCipher(asciiText);
        printMap(texts);
    }

    public static void repeatingXor() {
        String text = "1b420538554c2d100f2354096c44036c511838510f27101f235d096c430521400029101f39521f385918394405235e4c2f591c24551e62103823101e2954192f554c3858096c530321400029480538494c23564c3858053f100322554c3b554c3b59002010193f554c235e003510193c40093e530d3f554c20551838551e3f1c4c3f5f4c3858096c5b0935431c2d53096c591f6c5f0220494c7e064d6c64036c570938101824591f6c5f0229101e25570438100d39440321511825530d205c156c490339101b255c006c401e23520d2e5c156c5e0929544c385f4c3943096c430321554c3f5f1e3810032a100b295e0938590f6c51002b5f1e2544042110443b58052f584c3b5f1e2755086c440429100e2943186c5c0d3f444c35550d3e19406c43052145002d440928100d225e092d5c0522574c23424c2b420d28590922444c28551f2f5502381e4c1f551e255f193f5c1560101b3e591829100538101e2557043810022347406c490339101b255c006c5e0929544c25444c385f4c28550f25400429424c3858096c5e0934444c235e096c511f6c4709205c426c72092d424c255e4c215902281c4c3858093e558ecca91f6c5e036c431c2d53093f1e4c46581838401f761f43285f0f3f1e0b235f0b2055422f5f016354032f4501295e186354437d78357b74006105053869287f73332b5b1929721a3a722a3c64580f550f0b60051e035c0e432d1e5c383c6143295405380f193f40513f580d3e59022b";
        String asciiText = convertHex(text, StandardCharsets.UTF_8);
        RepeatingKeyXorCipher repeatingKeyXorCipher = new RepeatingKeyXorCipher();
        Map<String, String> texts = repeatingKeyXorCipher.hackRepeatingXorCipher(asciiText);
        printMap(texts);
    }

    public static String convertHex(String hex, Charset charset) {
        if (hex.length() % 2 != 0) {
            throw new RuntimeException("Invalid Hex");
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < hex.length(); i += 2) {
            String oneByte = hex.substring(i, i + 2);
            int number = Integer.valueOf(oneByte, 16);
            sb.append((char) number);
        }

        return new String(sb.toString().getBytes(), charset);
    }

    public static void printMap(Map<?, ?> map) {
        for (Object key : map.keySet()) {
            System.out.println("Key: " + key + ", Value: " + map.get(key));
        }
    }
}
