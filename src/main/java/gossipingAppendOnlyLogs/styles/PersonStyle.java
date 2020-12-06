package gossipingAppendOnlyLogs.styles;

import java.awt.Color;
import java.awt.Font;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.scene.VSpatial;

public class PersonStyle extends DefaultStyleOGL2D {
	@Override
	public Color getColor(Object o) {
		return Color.BLACK;
	}

	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		return shapeFactory.createCircle(1, 3);
	}
	
	@Override
	public Font getLabelFont(Object o) {
		return new Font("TimesRoman", Font.PLAIN, 9);
	}
	
	@Override
	public float getScale(Object o) {
		return 5f;
	}
}