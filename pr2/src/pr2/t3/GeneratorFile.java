package pr2.t3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GeneratorFile implements IFileDataGetter {
    private final String[] arrTypes;
    private final int MIN_SIZE;
    private final int MAX_SIZE;
    private final int MIN_PERIOD;
    private final int MAX_PERIOD;

    private final List<FileData> fileDataArrayList;

    public GeneratorFile(String[] arrTypes, int MIN_SIZE, int MAX_SIZE, int MIN_PERIOD, int MAX_PERIOD) {
        this.arrTypes = arrTypes;
        this.MIN_SIZE = MIN_SIZE;
        this.MAX_SIZE = MAX_SIZE;
        this.MIN_PERIOD = MIN_PERIOD;
        this.MAX_PERIOD = MAX_PERIOD;

        this.fileDataArrayList = Collections.synchronizedList(new ArrayList<FileData>());

        this.generate();
    }

    @Override
    public FileData getFileData() {
        return fileDataArrayList.remove(0);
    }

    private void generate() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run()
            {
                fileDataArrayList.add(new FileData(generateType(), generateSize()));
            }
        };

        timer.schedule(task, 0, generatePeriod());
    }

    private String generateType() {
        int index = (int) Math.floor(Math.random()  * this.arrTypes.length);

        return this.arrTypes[index];
    }

    private int generateSize() {
        return generateNumberInRange(MIN_SIZE, MAX_SIZE);
    }

    private int generatePeriod() {
        return generateNumberInRange(MIN_PERIOD, MAX_PERIOD);
    }

    private int generateNumberInRange(int min, int max) {
        return min + (int) Math.round(Math.random() * (max - min));
    }
}
