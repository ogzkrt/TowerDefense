package com.javakaian.game.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.javakaian.game.buttons.Menu;
import com.javakaian.game.map.GameConstants;
import com.javakaian.game.map.GameUtils;

public class HeaderMenu extends Menu {

	private BitmapFont bitmapFont;

	private String scoreText = "SCORE: ";
	private String moneyText = "MONEY: ";
	private String towerDamageText = "DAMAGE: ";
	private String towerRangeText = "RANGE: ";
	private String towerSpeedText = "SPEED: ";

	private int money;

	public HeaderMenu(Sprite menuSprite) {
		super(0, 0, GameConstants.SCREEN_WIDTH, GameConstants.GRID_HEIGHT * 2, menuSprite);
		this.bitmapFont = GameUtils.generateBitmapFont(25, Color.BLACK);
	}

	@Override
	public void render(ShapeRenderer sr) {
		super.render(sr);
	}

	@Override
	public void render(SpriteBatch sb) {
		renderGrids(sb);
		super.render(sb);
		renderBitmapFonts(sb);
	}

	private void renderBitmapFonts(SpriteBatch sb) {

		bitmapFont.draw(sb, towerDamageText, GameConstants.DAMAGE_POS_X, GameConstants.DAMAGE_POS_Y);
		bitmapFont.draw(sb, towerRangeText, GameConstants.RANGE_POS_X, GameConstants.RANGE_POS_Y);
		bitmapFont.draw(sb, towerSpeedText, GameConstants.SPEED_POS_X, GameConstants.SPEED_POS_Y);

		bitmapFont.draw(sb, scoreText, GameConstants.SCORE_POS_X, GameConstants.SCORE_POS_Y);
		bitmapFont.draw(sb, moneyText, GameConstants.MONEY_POS_X, GameConstants.MONEY_POS_Y);

	}

	private void renderGrids(SpriteBatch sb) {
		int row = 1;
		int col = 16;
		float posx = 0;
		float posy = 0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				posx = j * GameConstants.GRID_WIDTH;
				posy = i * GameConstants.GRID_HEIGHT;
				sb.draw(menuSprite, posx, posy, GameConstants.GRID_WIDTH, GameConstants.GRID_HEIGHT);
			}
		}
	}

	private void updateTexts() {
		moneyText = "MONEY: " + String.valueOf(money);
	}

	public void updateTowerInformations(float damage, float range, float attackSpeed) {
		towerDamageText = "DAMAGE: " + String.format("%.00f", damage);
		towerRangeText = "RANGE: " + String.format("%.00f", range);
		towerSpeedText = "SPEED: " + String.format("%.02f", attackSpeed);
	}

	public void fireMoneyChanged(int amount) {
		this.money = amount;
		updateTexts();
	}

}
