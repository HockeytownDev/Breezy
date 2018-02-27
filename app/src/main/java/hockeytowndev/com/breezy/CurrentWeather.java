package hockeytowndev.com.breezy;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Matt on 2/12/2018.
 */

public class CurrentWeather {

    private int mIcon;
    private String mTemperature;
    private String mTempLow;
    private String mTempHigh;
    private double mWindSpeed;
    private String mSummary;
    private String mLocation;
    private String mWindDegrees;
    private int mCondition;


    public static CurrentWeather fromJson (JSONObject jsonObject) {

        try {
            CurrentWeather currentWeather = new CurrentWeather();

            //City Name
            currentWeather.mLocation = jsonObject.getString("name");

            //Temp Low
            double tempLowResult = (jsonObject.getJSONObject("main").getDouble("temp_min") - 273.15) * 9.0/5.0 + 32;
            int roundTempLow = (int) Math.rint(tempLowResult);
            currentWeather.mTempLow = Integer.toString(roundTempLow);

            //Temp High
            double tempHighResult = (jsonObject.getJSONObject("main").getDouble("temp_max") - 273.15) * 9.0/5.0 + 32;
            int roundTempHigh = (int) Math.rint(tempHighResult);
            currentWeather.mTempHigh = Integer.toString(roundTempHigh);

            //Temperature
            double tempResult = (jsonObject.getJSONObject("main").getDouble("temp") - 273.15) * 9.0/5.0 + 32;
            int roundedValue = (int) Math.rint(tempResult);
            currentWeather.mTemperature = Integer.toString(roundedValue);

            //Wind Speed
            currentWeather.mWindSpeed = jsonObject.getJSONObject("wind").getDouble("speed");

            //Wind Degrees
            double windResult = (jsonObject.getJSONObject("wind").getDouble("deg"));
            int roundedDegrees = (int) Math.rint(windResult);
            currentWeather.mWindDegrees = Integer.toString(roundedDegrees);

            //Summary
            currentWeather.mSummary = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");

            //Weather Icon
            currentWeather.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            currentWeather.mIcon = getWeatherIcon(currentWeather.mCondition);



            return currentWeather;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

        private static int getWeatherIcon(int condition)
    {
         //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night
        int iconId = R.drawable.clear_day;

        if (condition == 904 || condition == 800)
        {
            iconId = R.drawable.clear_day;
        }
        else if (condition >= 300 || condition <= 700 ) {
            iconId = R.drawable.rain;
        }
        else if (condition >= 600 || condition <= 610) {
            iconId = R.drawable.snow;
        }
        else if (condition >= 615 || condition <= 650) {
            iconId = R.drawable.snow;
        }
        else if (condition >= 611 || condition <= 613) {
            iconId = R.drawable.sleet;
        }
        else if (condition >= 950 || condition <= 962) {
            iconId = R.drawable.wind;
        }
        else if (condition >= 700 || condition <= 790) {
            iconId = R.drawable.fog;
        }
        else if (condition >= 801 || condition <= 804) {
            iconId = R.drawable.partly_cloudy;
        }
        else if (condition >= 200 || condition <= 250) {
            iconId = R.drawable.rain;
        }
        else if (condition >= 801 || condition <= 804){
            iconId = R.drawable.cloudy;
        }

        return iconId;
    }


    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }

    public String getTemperature() {

        return mTemperature;
    }

    public void setTemperature(String temperature) {
        mTemperature = temperature;
    }

    public String getTempLow() {
        return mTempLow;
    }

    public void setTempLow(String tempLow) {
        mTempLow = tempLow;
    }

    public String getTempHigh() {
        return mTempHigh;
    }

    public void setTempHigh(String tempHigh) {
        mTempHigh = tempHigh;
    }

    public double getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        mWindSpeed = windSpeed;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getWindDegrees() {
        return mWindDegrees;
    }

    public void setWindDegrees(String windDegrees) {
        mWindDegrees = windDegrees;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String locationLabel) {
        mLocation = locationLabel;
    }

    public int getCondition() {
        return mCondition;
    }

    public void setCondition(int condition) {
        mCondition = condition;
    }
}
