public class Hospital {

    public static final float MIN_HEALTHY_TEMPERATURE = 36.2f;
    public static final float MAX_HEALTHY_TEMPERATURE = 36.9f;
    public static final float MIN_TEMPERATURE_GENERATED = 32.0f;
    public static final float MAX_TEMPERATURE_GENERATED = 40.0f;
    public static final float EPS = 0.01f;


    public static float[] generatePatientsTemperatures(int patientsCount) {
        float[] patientTemperatures = new float[patientsCount];
        for(int i = 0; i < patientsCount; i++){
            patientTemperatures[i] = (float)(MIN_TEMPERATURE_GENERATED + Math.random() *
                    (MAX_TEMPERATURE_GENERATED - MIN_TEMPERATURE_GENERATED));
        }
        return patientTemperatures;
    }

    public static String getReport(float[] temperatureData) {
        /*
        TODO: Напишите код, который выводит среднюю температуру по больнице,количество здоровых пациентов,
            а также температуры всех пациентов.
        */
        String temperatures = "";
        float sumTemperature = 0.0f;
        int countHealthy = 0;
        for(int i = 0; i < temperatureData.length; i++){
            if(i != 0) {
                temperatures = temperatures.concat(" ");
            }
            double resTemperature = (double) Math.round(10 * temperatureData[i]) / 10;
            temperatures = temperatures.concat( String.valueOf(resTemperature) );

            sumTemperature += temperatureData[i];
            if (temperatureData[i] > MIN_HEALTHY_TEMPERATURE - EPS
                    && temperatureData[i] < MAX_HEALTHY_TEMPERATURE + EPS) {
                countHealthy++;
            }
        }

        float averageTemperature = 0.0f;
        if (temperatureData.length != 0) {
            averageTemperature = sumTemperature / temperatureData.length;
        }
        averageTemperature = (float) Math.round(averageTemperature * 100) / 100;

        String report =
                "Температуры пациентов: " + temperatures +
                        "\nСредняя температура: " + averageTemperature +
                        "\nКоличество здоровых: " + countHealthy;

        return report;
    }
}
