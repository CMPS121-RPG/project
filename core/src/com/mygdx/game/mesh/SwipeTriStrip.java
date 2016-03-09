package com.mygdx.game.mesh;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;

public class SwipeTriStrip {

	Array<Vector2> texcoord = new Array<Vector2>();
	Array<Vector2> tristrip = new Array<Vector2>();
	int batchSize;
	Vector2 perp = new Vector2();
	public float thickness = 30f;
	public float endcap = 8.5f;
	public Color color = new Color(Color.WHITE);
	ImmediateModeRenderer20 gl20;
	float alpha = 1;
	double previousTime = 0;
	double alphaDecayRate = 0.004;
	
	public SwipeTriStrip() {
		gl20 = new ImmediateModeRenderer20(false, true, 1);
		previousTime = System.currentTimeMillis();
	}
	
	public void draw(Camera cam, boolean isDown) {

		if (tristrip.size<=0)
			return;

//		System.out.println(tristrip);

		double curTime = System.currentTimeMillis();

		if (!isDown && alpha > 0) {
			double timeElapsed = curTime - previousTime;
			alpha -= timeElapsed * alphaDecayRate;
			if (alpha < 0) {
				alpha = 0;
			}
//			System.out.println(alpha + " : " + timeElapsed);

		}

		previousTime = curTime;

//		System.out.println(alpha);

		gl20.begin(cam.combined, GL20.GL_TRIANGLE_STRIP);
		for (int i=0; i<tristrip.size; i++) {
			if (i==batchSize) {
				gl20.end();
				gl20.begin(cam.combined, GL20.GL_TRIANGLE_STRIP);
			}	
			Vector2 point = tristrip.get(i);
			Vector2 tc = texcoord.get(i);
//			gl20.color(color.r, color.g, color.b, color.a);
			gl20.color(color.r, color.g, color.b, alpha);
			gl20.texCoord(tc.x, 0f);
			gl20.vertex(point.x, point.y, 0f);
		}
		gl20.end();
	}

	public void resetAlpha() {
		alpha = 1;
	}

	private int generate(Array<Vector2> input, int mult) {
		int c = tristrip.size;
		if (endcap<=0) {
			tristrip.add(input.get(0));
		} else {
			Vector2 p = input.get(0);
			Vector2 p2 = input.get(1);
			//perp.set(p).sub(p2).mul(endcap);
			perp.set(p).sub(p2).scl(endcap); // changed from mul to scl
			tristrip.add(new Vector2(p.x+perp.x, p.y+perp.y));
		}
		texcoord.add(new Vector2(0f, 0f));
		
		for (int i=1; i<input.size-1; i++) {
			Vector2 p = input.get(i);
			Vector2 p2 = input.get(i+1);
			
			//get direction and normalize it
			perp.set(p).sub(p2).nor();
			
			//get perpendicular
			perp.set(-perp.y, perp.x);
			
			float thick = thickness * (1f-((i)/(float)(input.size)));
			
			//move outward by thickness
			//perp.mul(thick/2f);
			perp.scl(thick / 2f); // changed from mul to scl
			
			//decide on which side we are using
			//perp.mul(mult);
			perp.scl(mult); // changed from mul to scl
			
			//add the tip of perpendicular
			tristrip.add(new Vector2(p.x+perp.x, p.y+perp.y));
			//0.0 -> end, transparent
			texcoord.add(new Vector2(0f, 0f));
			
			//add the center point
			tristrip.add(new Vector2(p.x, p.y));
			//1.0 -> center, opaque
			texcoord.add(new Vector2(1f, 0f));
		}
		
		//final point
		if (endcap<=0) {
			tristrip.add(input.get(input.size-1));
		} else {
			Vector2 p = input.get(input.size-2);
			Vector2 p2 = input.get(input.size-1);
			//perp.set(p2).sub(p).mul(endcap);
			perp.set(p2).sub(p).scl(endcap); // changed from mul to scl
			tristrip.add(new Vector2(p2.x+perp.x, p2.y+perp.y));
		}
		//end cap is transparent
		texcoord.add(new Vector2(0f, 0f));
		return tristrip.size-c;
	}
	
	public void update(Array<Vector2> input) {
		tristrip.clear();
		texcoord.clear();
		
		if (input.size<2)
			return;
		batchSize = generate(input, 1);
		int b = generate(input, -1);
	}
	
}
