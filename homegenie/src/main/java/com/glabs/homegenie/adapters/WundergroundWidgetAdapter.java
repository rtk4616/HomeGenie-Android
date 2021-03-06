/*
    This file is part of HomeGenie for Adnroid.

    HomeGenie for Adnroid is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    HomeGenie for Adnroid is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with HomeGenie for Adnroid.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
 *     Author: Generoso Martello <gene@homegenie.it>
 */

package com.glabs.homegenie.adapters;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.glabs.homegenie.R;
import com.glabs.homegenie.client.data.Module;
import com.glabs.homegenie.client.data.ModuleParameter;
import com.glabs.homegenie.data.ModuleHolder;

/**
 * Created by Gene on 05/01/14.
 */
public class WundergroundWidgetAdapter extends GenericWidgetAdapter {

    public WundergroundWidgetAdapter(ModuleHolder module) {
        super(module);
    }

    @Override
    public View getView(LayoutInflater inflater) {
        View v = _moduleHolder.View;
        if (v == null) {
            v = inflater.inflate(R.layout.widget_item_wunderground, null);
            _moduleHolder.View = v;
            v.setTag(_moduleHolder);
        } else {
            v = _moduleHolder.View;
        }
        return v;
    }

    @Override
    public void updateViewModel() {

        if (_moduleHolder.View == null) return;

        TextView title = (TextView) _moduleHolder.View.findViewById(R.id.titleText);
        TextView subtitle = (TextView) _moduleHolder.View.findViewById(R.id.subtitleText);
        TextView infotext = (TextView) _moduleHolder.View.findViewById(R.id.infoText);

        title.setText(_moduleHolder.Module.getDisplayName());
        subtitle.setText(_moduleHolder.Module.getDisplayAddress());
        infotext.setVisibility(View.GONE);
        //
        ModuleParameter sunriseParam = _moduleHolder.Module.getParameter("Astronomy.Sunrise");
        String sunrise = "";
        if (sunriseParam != null) sunrise = sunriseParam.Value;
        _updatePropertyBox(_moduleHolder.View, R.id.propSunrise, "Sunrise", sunrise);
        //
        ModuleParameter sunsetParam = _moduleHolder.Module.getParameter("Astronomy.Sunset");
        String sunset = "";
        if (sunsetParam != null) sunset = sunsetParam.Value;
        _updatePropertyBox(_moduleHolder.View, R.id.propSunset, "Sunset", sunset);
        //
        ModuleParameter displayCelsius = _moduleHolder.Module.getParameter("Conditions.DisplayCelsius");
        if (displayCelsius != null && displayCelsius.Value.toLowerCase().equals("true"))
        {
            ModuleParameter sensorTemperature = _moduleHolder.Module.getParameter("Conditions.TemperatureC");
            String temperature = "";
            if (sensorTemperature != null)
                temperature = Module.getFormattedNumber(sensorTemperature.Value);
            _updatePropertyBox(_moduleHolder.View, R.id.propTemperature, "Temp.℃", temperature);
        }
        else
        {
            ModuleParameter sensorTemperature = _moduleHolder.Module.getParameter("Conditions.TemperatureF");
            String temperature = "";
            if (sensorTemperature != null)
                temperature = Module.getFormattedNumber(sensorTemperature.Value);
            _updatePropertyBox(_moduleHolder.View, R.id.propTemperature, "Temp.℉", temperature);
        }
        //
        ModuleParameter sensorPressure = _moduleHolder.Module.getParameter("Conditions.PressureMb");
        String pressure = "";
        if (sensorPressure != null) pressure = Module.getFormattedNumber(sensorPressure.Value);
        _updatePropertyBox(_moduleHolder.View, R.id.propPressure, "Press.mb", pressure);
        //
        ModuleParameter sensorPrecipitations = _moduleHolder.Module.getParameter("Conditions.PrecipitationHourMetric");
        String precipitations = "";
        if (sensorPrecipitations != null)
            precipitations = Module.getFormattedNumber(sensorPrecipitations.Value);
        _updatePropertyBox(_moduleHolder.View, R.id.propPrecipitations, "Precip.h/m", precipitations);
        //
        ModuleParameter condLocation = _moduleHolder.Module.getParameter("Conditions.DisplayLocation");
        String location = "";
        if (condLocation != null) location = condLocation.Value;
        TextView tv1 = (TextView) _moduleHolder.View.findViewById(R.id.condLocation);
        tv1.setText(location);
        //
        ModuleParameter condDescription = _moduleHolder.Module.getParameter("Conditions.Description");
        String description = "";
        if (condDescription != null) description = condDescription.Value;
        TextView tv2 = (TextView) _moduleHolder.View.findViewById(R.id.condDescription);
        tv2.setText(description);

        String updateTimestamp = "";
        if (sunriseParam != null) {
            //updateTimestamp = new SimpleDateFormat("MMM y E dd - HH:mm:ss").format(sunriseParam.UpdateTime);
            updateTimestamp = DateFormat.getDateFormat(_moduleHolder.View.getContext()).format(sunriseParam.UpdateTime) + " " +
                    DateFormat.getTimeFormat(_moduleHolder.View.getContext()).format(sunriseParam.UpdateTime);

            infotext.setText(updateTimestamp);
            infotext.setVisibility(View.VISIBLE);
        }

        ModuleParameter iconUrl = _moduleHolder.Module.getParameter("Conditions.IconUrl");
        int imageres = 0;
        if (iconUrl != null) {
            String fname = iconUrl.Value.substring(iconUrl.Value.lastIndexOf('/') + 1);
            fname = fname.replace(".gif", "");
            if (fname.startsWith("nt_")) {
                fname = "weather_night_" + fname.replace("nt_", ""); // + ".png";
            } else {
                fname = "weather_day_" + fname; // + ".png";
            }
            imageres = _moduleHolder.View.getResources().getIdentifier(fname, "drawable", _moduleHolder.View.getContext().getApplicationContext().getPackageName());
        }
        final ImageView image = (ImageView) _moduleHolder.View.findViewById(R.id.iconImage);
        final String timestamp = updateTimestamp;
        if (imageres > 0 && (image.getTag() == null || !image.getTag().equals(timestamp))) {
            image.setImageResource(imageres);
            image.setTag(timestamp);
        }

    }

}
