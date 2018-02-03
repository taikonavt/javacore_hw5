public class Main {

    public static void main(String[] args) {
        float[] array1 = createArray();
        float[] array2 = createArray();

        firstMethod(array1);
        secondMethod(array2, 4);

        for (int i = 1; i < array1.length; i++) {
            System.out.println(array1[i] + " = " + array2[i]);
        }
    }

    private static float[] createArray() {
        final int size = 10000000;
        float[] array = new float[size];

        for (int i = 0; i < size; i++) {
            array[i] = 1;
        }
        return array;
    }

    private static void calculate(float[] array, int start) {
        for (int i = 0; i < array.length; i++) {
            int j = start + i;
            array[i] = (float) (array[i] * Math.sin(0.2f + j/5) * Math.cos(0.2f + j/5) * Math.cos(0.4f + j/2));
        }
    }

    private static void firstMethod(float[] array) {
        long a = System.currentTimeMillis();
        calculate(array, 0);
        System.out.println("first method = " + (System.currentTimeMillis() - a));
    }

    private static void secondMethod(float[] array, int amountOfThreads) {
        long a = System.currentTimeMillis();
        int[][] subIndex = new int[amountOfThreads][2];

        for (int i = 0; i < amountOfThreads; i++) {
            subIndex[i][0] = i * array.length / amountOfThreads;
            subIndex[i][1] = (i + 1) * array.length / amountOfThreads;
        }

        for (int i = 0; i < amountOfThreads; i++) {
            final int k = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int num = subIndex[k][1] - subIndex[k][0];
                    float[] subArray = new float[num];
                    System.arraycopy(array, subIndex[k][0], subArray,0, num);
                    calculate(subArray, subIndex[k][0]);
                    System.arraycopy(subArray, 0, array, subIndex[k][0], subArray.length);
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("second method = " + (System.currentTimeMillis() - a));
    }
}
