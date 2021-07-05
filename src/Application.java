import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Application {

    private static final String DIRECTORY = "output" + File.separator;
    private static final int ROWS = 100_0;
    private static final List<File> FILES = getList();
    private static final int CAPACITY = 10;

    public static void main(String[] args) {
        while (true) {
            System.out.println("Choose the option:\n1\t-\tmerge files\t2\t-\tdelete from all files\t3\t-\texit");
            Scanner in = new Scanner(System.in);
            int answer = in.nextInt();
            switch (answer) {
                case 1:
                    createGigaFile();
                    break;
                case 2:
                    editAllFiles();
                    break;
                case 3:
                    System.exit(0);
            }
        }
    }

    private static void editAllFiles() {
        System.out.println("Enter the key:");
        Scanner in = new Scanner(System.in);
        String key = in.nextLine();
        for (int i = 0; i < CAPACITY; i++) {
            List<String> fileList = new ArrayList<>(ROWS);
            File file = FILES.get(i);
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                while (line != null) {
                    if (!line.contains(key)) {
                        fileList.add(line);
                    }
                    line = br.readLine();
                }
                br.close();
                PrintWriter pw = new PrintWriter(file);
                for (String string : fileList) {
                    pw.println(string);
                }
                pw.close();
                fileList.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createGigaFile() {
        String gigaFileName = DIRECTORY + "gigafile";//+xls
        File gigaFile = new File(gigaFileName);

        try (PrintWriter pw = new PrintWriter(gigaFile)) {
            for (int i = 0; i < CAPACITY; i++) {
                BufferedReader br = new BufferedReader(new FileReader(FILES.get(i)));
                String line = br.readLine();
                while (line != null) {
                    pw.println(line);
                    line = br.readLine();
                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static List<File> getList() {
        List<File> files = new ArrayList<>(CAPACITY);
        File dir = new File(DIRECTORY);
        dir.mkdir();
        for (int i = 0; i < CAPACITY; i++) {
            files.add(createFile(i + 1));
        }
        return files;
    }

    private static File createFile(int number) {
        String fileName = "output" + File.separator + "output" + number;// + ".xls";
        String fileDelim = "||";

        File file = new File(fileName);
        for (int i = 0; i < ROWS; i++) {

            StringBuilder stringForFile = new StringBuilder();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MM:yyyy");
            Date randomDate = getTimeBetween(new Date(new Date().getTime() - TimeUnit.DAYS.toMillis(1) * 365 * 5)
                    , new Date(new Date().getTime()));

            String randomStringENG = getRandomString("eng");
            String randomStringRUS = getRandomString("rus");
            Integer randomInt = getRandomInt(1, 100_000_000);
            Double randomDouble = BigDecimal.valueOf(getRandomDouble(1, 20)).setScale(8, RoundingMode.HALF_UP).doubleValue();

            stringForFile.append(simpleDateFormat.format(randomDate)).append(fileDelim).append(randomStringENG).append(fileDelim).append(randomStringRUS)
                    .append(fileDelim).append(randomInt).append(fileDelim).append(randomDouble).append(fileDelim).append('\n');

            try (FileWriter fw = new FileWriter(file, true)) {
                fw.write(stringForFile.toString());
                fw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private static double getRandomDouble(double min, double max) {
        Random random = new Random();
        return random.nextDouble() * (max - min) + min;
    }

    private static int getRandomInt(int min, int max) {
        Random random = new Random();
        if (min < 2) min = 2;
        return random.nextInt((max - min) / 2) * 2 + min;
    }

    private static String getRandomString(String language) {

        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        int length = 10;
        switch (language.trim().toLowerCase()) {
            case ("eng"):
                String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
                String alphabet = lowerAlphabet + upperAlphabet;
                for (int i = 0; i < length; i++) {
                    int index = random.nextInt(alphabet.length());
                    char randomChar = alphabet.charAt(index);
                    stringBuilder.append(randomChar);
                }
                break;
            case ("rus"):
                alphabet = "АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя";
                for (int i = 0; i < length; i++) {
                    int index = random.nextInt(alphabet.length());
                    char randomChar = alphabet.charAt(index);
                    stringBuilder.append(randomChar);
                }
                break;
        }
        return stringBuilder.toString();
    }

    public static Date getTimeBetween(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
    }
}
