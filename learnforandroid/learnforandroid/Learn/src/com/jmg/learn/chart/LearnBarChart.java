/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Changed by J.M.Goebel Copyright (C) 2015
 * GPL 3
 *  This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jmg.learn.chart;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;
import com.jmg.learn.MainActivity;
import com.jmg.learn.vok.Vokabel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

/**
 * Sales demo bar chart.
 */
public class LearnBarChart extends AbstractDemoChart {

	/**
	 * Returns the chart name.
	 * 
	 * @return the chart name
	 */
	public String getName() {
		return "Sales horizontal bar chart";
	}

	/**
	 * Returns the chart description.
	 * 
	 * @return the chart description
	 */
	public String getDesc() {
		return "The monthly sales for the last 2 years (horizontal bar chart)";
	}

	/**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	public Intent execute(Context context) {
		MainActivity Main = (MainActivity) context;
		Vokabel vok = Main.vok;
		File F = new File(vok.getFileName());
		String[] titles = new String[] { F.getName() };
		List<double[]> values = new ArrayList<double[]>();
		double v[] = new double[14];
		for (int i = -6; i <= 6; i++) {
			v[i + 6] = vok.getLearned(i);
		}

		values.add(v);

		int[] colors = new int[] { Color.CYAN }; // Color.CYAN,Color.GREEN,Color.BLUE,Color.MAGENTA,Color.RED,Color.YELLOW};
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		renderer.setOrientation(Orientation.HORIZONTAL);

		setChartSettings(renderer, "Learned vocabulary for " + F.getName(),
				"Learnindex", "Words", 1, 14, 0, vok.getGesamtzahl(),
				Color.GREEN, Color.YELLOW);
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setApplyBackgroundColor(true);
		renderer.setXLabels(0);
		renderer.setYLabels(10);

		for (int i = -6; i <= 6; i++) {
			renderer.addXTextLabel(i + 7, "" + i);

		}

		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer seriesRenderer = renderer
					.getSeriesRendererAt(i);
			seriesRenderer.setDisplayChartValues(true);
			seriesRenderer.setGradientEnabled(true);
			seriesRenderer.setGradientStart(0, Color.CYAN);
			seriesRenderer.setGradientStop(vok.getGesamtzahl(), Color.RED);
		}
		return ChartFactory.getBarChartIntent(context,
				buildBarDataset(titles, values), renderer, Type.DEFAULT);
	}

}
