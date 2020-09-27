package com.javakaian.game.towers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.javakaian.game.entity.Bullet;
import com.javakaian.game.entity.Enemy;
import com.javakaian.game.entity.GameObject;
import com.javakaian.game.util.GameConstants;

public abstract class BaseTower extends GameObject {

	protected List<Enemy> enemyList;
	protected List<Bullet> bulletList;

	protected Enemy target;
	protected float range;
	protected float rotation = 0;
	protected float damage;

	public float attackSpeed = 2f; // times per second.
	protected float speedCounter = 0;

	protected boolean ice;

	protected int towerPrice;
	protected int rangePrice;
	protected int attackPrice;
	protected int speedPrice;

	protected TowerType type;

	protected HashMap<Enemy, Float> enemyMap;

	public enum TowerType {

		ELECTRIC, FIRE, ICE
	}

	protected boolean doubleSpeed = false;

	public BaseTower(float x, float y, float width, float height, List<Enemy> enemyList) {
		super(x, y, width, height);
		this.enemyList = enemyList;
		this.range = GameConstants.TOWER_RANGE;

		towerPrice = GameConstants.TOWER_PRICE;
		attackPrice = GameConstants.TOWER_ATTACK_PRICE;
		rangePrice = GameConstants.TOWER_RANGE_PRICE;
		speedPrice = GameConstants.TOWER_SPEED_PRICE;

		bulletList = new ArrayList<Bullet>();
	}

	@Override
	public void render(ShapeRenderer sr) {

		sr.setColor(Color.CYAN);
		if (isSelected) {
			sr.circle(center.x, center.y, range);
		}
		for (Bullet bullet : bulletList) {
			bullet.render(sr);
		}

	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		updateTargetMap();
		if (target == null) {
			findTarget();
		} else if (isInRange() && isTargetAlive()) {

			calculateRotation();
			invokeShootFunctions(deltaTime);
			removeBullets();
		}
		for (Bullet bullet : bulletList) {
			bullet.update(deltaTime);
		}
	}

	public void render(SpriteBatch sb) {

		if (isSelected) {

			sb.draw(spriteSelected, position.x, position.y, size.x / 2, size.y / 2, size.x, size.y, 1, 1, rotation);
		} else {

			sb.draw(sprite, position.x, position.y, size.x / 2, size.y / 2, size.x, size.y, 1, 1, rotation);
		}

		for (Bullet bullet : bulletList) {
			bullet.render(sb);
		}

	}

	private void calculateRotation() {

		Vector2 temp = new Vector2(target.center).sub(center);
		float angle = temp.angle() + 90f;
		rotation = angle;
	}

	public abstract void projectileShoot();

	public abstract void contiuniousShoot();

	public abstract void onTargetFound();

	public abstract void onTargetLost();

	private void invokeShootFunctions(float deltaTime) {

		contiuniousShoot();
		speedCounter += deltaTime;
		if (speedCounter >= 1.0f / attackSpeed) {
			speedCounter = 0;
			projectileShoot();
		}
	}

	private void removeBullets() {
		List<Bullet> tempList = new ArrayList<Bullet>();
		for (int i = 0; i < bulletList.size(); i++) {
			Bullet bullet = bulletList.get(i);
			if (!bullet.isVisible()) {
				tempList.add(bullet);
			}
		}
		bulletList.removeAll(tempList);

	}

	private boolean isTargetAlive() {
		if (!target.isAlive()) {
			setTarget(null);
			return false;
		}
		return true;
	}

	private boolean isInRange() {
		float distance = center.dst(target.position);
		if (distance > range) {
			setTarget(null);
		}
		return distance <= range;

	}

	private void updateTargetMap() {
		enemyMap = new HashMap<>();
		for (Enemy enemy : enemyList) {
			float distance = center.dst(enemy.position);
			if (distance <= range && enemy.isAlive()) {
				enemyMap.put(enemy, distance);
			} else {
				enemyMap.remove(enemy);
			}
		}

	}

	private void findTarget() {

		Collection<Float> values = enemyMap.values();
		if (!values.isEmpty()) {

			float lowest = Collections.min(values);

			for (Entry<Enemy, Float> entry : enemyMap.entrySet()) {
				if (entry.getValue().equals(lowest)) {
					target = entry.getKey();
					setTarget(entry.getKey());
				}
			}

		}
	}

	public void setTarget(Enemy target) {
		if (target == null) {
			onTargetLost();
			this.target = null;
		} else {
			this.target = target;
			onTargetFound();
		}
	}

	public Enemy getTarget() {
		return target;
	}

	public void setType(TowerType type) {
		this.type = type;
	}

	public TowerType getType() {
		return type;
	}

	public float getRange() {
		return range;
	}

	public float getDamage() {
		return damage;
	}

	public float getSpeed() {
		return attackSpeed;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void doubleSpeedClicked() {
		doubleSpeed = true;
	}

	public void normalSpeedClicked() {
		doubleSpeed = false;
	}

	public void increaseDamage() {
		this.damage += 1;
		attackPrice += 1;
	}

	public void increaseRange(float range) {
		this.range += range;
		rangePrice += 1;

	}

	public void increaseSpeed() {
		this.attackSpeed *= 1.1;
		speedPrice += 1;
	}

	public int getTowerPrice() {
		return towerPrice;
	}

	public int getRangePrice() {
		return rangePrice;
	}

	public int getAttackPrice() {
		return attackPrice;
	}

	public int getSpeedPrice() {
		return speedPrice;
	}

}
