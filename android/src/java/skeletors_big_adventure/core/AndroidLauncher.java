package skeletors_big_adventure.core;

import clojure.lang.RT;
import clojure.lang.Symbol;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.Game;

public class AndroidLauncher extends AndroidApplication {
	public void onCreate (android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
          RT.var("clojure.core", "require").invoke(Symbol.intern("skeletors-big-adventure.core"));
		try {
			Game game = (Game) RT.var("skeletors-big-adventure.core", "skeletors-big-adventure").deref();
			initialize(game, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
