package gossipingAppendOnlyLogs.Styles;

import java.awt.Color;
import java.awt.Font;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.scene.VSpatial;

public class LANStyle extends DefaultStyleOGL2D {
	@Override
	public Color getColor(Object o) {
		Color color = Color.ORANGE;
		
		int alpha = 150;
		
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}

	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		int radius = 10;
		return shapeFactory.createCircle(radius, 30);
	}
	
	@Override
	public Font getLabelFont(Object o) {
		return new Font("TimesRoman", Font.PLAIN, 9);
	}
	
	@Override
	public float getScale(Object o) {
		return 15f;
	}
}