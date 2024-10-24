import java.awt.Graphics;
import agents.controllers.MarioAIBase;
import engine.core.IEnvironment;
import engine.core.LevelScene;
import engine.graphics.VisualizationComponent;
import engine.input.*;

public class MyAgent extends MarioAIBase {

	private static final int JUMP_DURATION = 10; // Number of frames to hold the jump
	private int jumpCounter = 0; // Counter for jump duration

	@Override
	public void debugDraw(VisualizationComponent vis, LevelScene level, IEnvironment env, Graphics g) {
		super.debugDraw(vis, level, env, g);
		if (mario == null) return;

		// EXAMPLE DEBUG VISUALIZATION
		String debug = "MY DEBUG STRING";
		if (enemyAhead()) debug += " |ENEMY AHEAD|";
		if (wallAhead()) debug += " |BRICK AHEAD|";
		if (wallAhead()) debug += " |Drop AHEAD|";
		VisualizationComponent.drawStringDropShadow(g, debug, 0, 26, 1);
	}
	// Called on each tick to find out what action(s) Mario should take.
	@Override
	public MarioInput actionSelectionAI() {
		MarioInput input = new MarioInput();
		input.press(MarioKey.RIGHT); // Always run right

		// Shoot whenver possible
		if (mario.mayShoot) {
			// Only press the speed key if it's not already pressed
			if (!lastInput.isPressed(MarioKey.SPEED)) {
				input.press(MarioKey.SPEED); // Activate shooting
			}
		} else {
			input.press(MarioKey.SPEED); // Keep sprinting if not able to shoot
		}

		if (mario.mayJump && (enemyAhead() || wallAhead() || emptyTileBelow()))
		{
				input.press(MarioKey.JUMP);
				jumpCounter = JUMP_DURATION; // Reset the jump counter

		}

		// Hold the jump button down
		if (jumpCounter > 0) {
			input.press(MarioKey.JUMP);
			jumpCounter--; // Decrease the jump counter
		}

		return input;
	}


	private boolean enemyAhead() {
		// Check for enemies
		return entities.danger(1, 0) || entities.danger(1, -1) || entities.danger(2, 0);
	}

	private boolean wallAhead() {
		// Check for walls ahead
		return tiles.brick(1, 0) || tiles.brick(1, -1) || tiles.brick(2, 0) || tiles.brick(2, -1)
				|| tiles.brick(3, 0) || tiles.brick(3, -1);
	}

	private boolean emptyTileBelow() {
		// Check for a drop coming up ahead
		return tiles.emptyTile(1, 1);
	}

}