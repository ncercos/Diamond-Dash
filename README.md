
<p align="center">
  <a href="https://youtu.be/m_qE0tDzJ1s?si=k7eZ-Vng8DPyw6LM" target="_blank" rel="noreferrer"><img src="https://i.imgur.com/Vwp7U66.png" alt="Diamond Dash"></a>
</p>


[Diamond Dash](https://youtu.be/m_qE0tDzJ1s?si=k7eZ-Vng8DPyw6LM) is a thrilling 2D Java platformer that challenges players to navigate through two distinct levels—a lush, vibrant landscape and a barren, arid terrain. In each level, your goal is to collect all the scattered diamonds to advance and ultimately win the game. Along the way, you'll encounter hostile enemies that guard the precious gems, adding an extra layer of excitement and danger to your quest. Sharpen your skills and prepare for an adventure where every diamond counts!
&nbsp;

<p align="center">
  <a href="https://youtu.be/m_qE0tDzJ1s?si=k7eZ-Vng8DPyw6LM">
    <img src="https://i.imgur.com/4k3xDWD.gif" alt="Diamond Dash Demo">
  </a>
</p>

## 🎮 How to Play  
  
**Objective**: Collect all diamonds in each level to progress. Navigate through two unique environments while avoiding or battling hostile enemies that guard these precious gems.  
  
- `A` - Move Left  
- `D` - Move Right  
- `SPACE` - Jump  
- `Left Mouse Button` - Attack  
- `Right Mouse Button` - Roll  
- `ESC` - Pause Game
&nbsp;

## ⚙️ Technologies Used

-   **Java** - Core programming language
-   **Java Swing (javax.swing)** - For GUI components (JFrame, JPanel, JButton)
-   **Java AWT (java.awt)** - For graphics, events, and layouts
&nbsp;

## ✨ Key Features

### 1. Double-Buffered Graphics Engine

Smooth, flicker-free rendering system using double buffering technique for optimal visual performance.
&nbsp;

**How it works**: Creates an off-screen buffer for drawing before displaying to prevent screen tearing.

```java
public class GamePanel extends JPanel {
    private Image scene;
    private Graphics pen;
    
    public void paintComponent(Graphics g) {
        if(scene == null) {
            scene = createImage(GAME_WIDTH, GAME_HEIGHT);
            pen = scene.getGraphics();
        }
        pen.clearRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        game.draw(pen);
        g.drawImage(scene, 0, 0, this);
    }
}
```

### 2. Multi-Layer Level Rendering System

Sophisticated level rendering system with multiple layers for depth and visual complexity, including midground, foreground, flora, and water layers.  
&nbsp;

**How it works**: Layers are rendered in specific order for proper visual composition and game object placement.

```java
public enum LevelLayer {
    MIDGROUND,  // Base terrain
    DECOR,      // Decorative elements
    FLORA,      // Plants and vegetation
    SPAWNS,     // Entity spawn points
    FOREGROUND, // Front elements
    WATER;      // Water effects
}

public class Level {
	public void draw(Graphics g) {
		for (LevelLayer layer : LevelLayer.values()) {
			if (layer.equals(LevelLayer.SPAWNS)) {
				// ...spawn logic...
			} else drawLayer(g, layer);
		}
	}
}
```

### 3. Sprite Animation System

Dynamic animation system supporting frame-based animations with customizable durations, cycles, and state management.  
&nbsp;

**How it works**: Automatically handles sprite sheet parsing and animation cycles with configurable parameters.

```java
public class Animation {

	private final Image[] images;
	private int size;
	private final int duration;
	private int current, delay;
	private boolean cycleCompleted;
	private boolean frozen = false, repeatable = true;
	
	public Image[] loadImages(String name) {
		BufferedImage sprite = Game.loadSprite(name + ".png");
		if(sprite == null) return null;
		final int WIDTH = sprite.getWidth() / size;
		Image[] images = new Image[WIDTH];

		for(int i = 0; i < WIDTH; i++)
			images[i] = sprite.getSubimage(i * size, 0, size, size);
		return images;
	}
	
	public Image getCurrentImage(Playing playing) {
		if(!playing.isPaused()) delay--;
		if(delay == 0) {
			current++;
			if(current == images.length) {
				current = repeatable ? 0 : (images.length - 1);
				cycleCompleted = true;
			}
			delay = duration;
		}
		return images[current];
	}
}
```

### 4. Physics & Tile Collisions

Tile-based collision detection integrated with a physics system that provides realistic movement and interactions.  
&nbsp;

**How it works**: Combines precise tile collision checking with continuous physics calculations for smooth movement and realistic environmental interactions.

```java
private void updateXPos() {
	if(canMoveTo(x + vx, y)) setX(x + vx);
	else setX(getXPosNextToTile());
}

private void updateYPos() {
	if(canMoveTo(x, y + vy)) {
		y += vy;
		if(vy < MAX_FALL_VELOCITY) vy += GRAVITY;
	} else {
		y = getYPosAboveOrUnderTile();
		if(vy > 0) stopFalling();
		else vy = GRAVITY;
	}
}

public boolean canMoveTo(double x, double y) {
	// Check all four corners of entity for collision
	if(!getLevel().isSolid(x, y))
		if(!getLevel().isSolid(x + w, y + h))
			if(!getLevel().isSolid(x + w, y))
				return !getLevel().isSolid(x, y + h);
	return false;
}

protected double getXPosNextToTile() {
	int tileXPos = getTileX() * Game.TILES_SIZE;
	if(vx > 0) { /* moving to the right */
		int xOffset = (int)(Game.TILES_SIZE - w);
		return tileXPos + xOffset - 1;
	} else return tileXPos;
}

private double getYPosAboveOrUnderTile() {
	int tileYPos = getTileY() * Game.TILES_SIZE;
	if(vy > 0) { /* falling */
		int yOffset = (int)(Game.TILES_SIZE - h);
		return tileYPos + yOffset - 1;
	} else return tileYPos;
}
```

### 5. Dynamic Enemy AI

Unique enemy behavior for all hostile mobs with real-time tracking and combat mechanics. Enemies actively pursue the player when in range and engage in combat based on distance calculations.  
&nbsp;

**How it works**: Enemies use sight distance calculations and player position tracking to determine their actions.

```java
public class Slime extends Hostile {
    public void update() {
        if(isInSight(player)) {
            if(!isInAttackRange(player)) {
                turnTowards(player);
                double speed = 1;
                if (facingLeft) goLT(speed);
                else goRT(speed);
            } else if(isAbleToAttack()) {
                if(player.overlaps(this))
                    attack(player);
            }
        }
    }
}
```

## 📚 Extended Code Walkthrough
For a detailed explanation of the codebase and implementation details, check out this comprehensive video walkthrough **[here](https://youtu.be/DTcJq4k3OIY?si=L-m0hkogo-WQ7baq)**!
