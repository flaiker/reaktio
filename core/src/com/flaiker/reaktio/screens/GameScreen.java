/***********************************************************************************
 * The MIT License (MIT)                                                           *
 *                                                                                 *
 * Copyright (c) 2015 Fabian Lupa                                                  *
 *                                                                                 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy    *
 * of this software and associated documentation files (the "Software"), to deal   *
 * in the Software without restriction, including without limitation the rights    *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell       *
 * copies of the Software, and to permit persons to whom the Software is           *
 * furnished to do so, subject to the following conditions:                        *
 *                                                                                 *
 * The above copyright notice and this permission notice shall be included in all  *
 * copies or substantial portions of the Software.                                 *
 *                                                                                 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR      *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,        *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE     *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER          *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,   *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE   *
 * SOFTWARE.                                                                       *
 ***********************************************************************************/

package com.flaiker.reaktio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.flaiker.reaktio.Reaktio;
import com.flaiker.reaktio.game.Game;
import com.flaiker.reaktio.game.GameMode;
import com.flaiker.reaktio.game.GameSettings;
import com.flaiker.reaktio.helper.Tools;

public class GameScreen extends AbstractScreen {
    public static final String LOG = GameScreen.class.getSimpleName();

    private Label gameTimeLabel = new Label("60.0", skin, "digital7-92", Color.WHITE);
    private Label infoLabel     = new Label("Touch to start!", skin, "digital7-64", Color.WHITE);
    private Label scoreLabel    = new Label("", skin);

    private final Game actualGame;

    public GameScreen(Reaktio reaktio) {
        super(reaktio);
        actualGame = new Game(GameSettings.newContinuousGameSettings(2, 2), SCREEN_WIDTH, SCREEN_HEIGHT, camera);
    }

    public GameScreen(Reaktio reaktio, Skin skin) {
        super(reaktio, skin);
        actualGame = new Game(GameSettings.newContinuousGameSettings(2, 2), SCREEN_WIDTH, SCREEN_HEIGHT, camera);
    }

    @Override
    protected String getName() {
        return LOG;
    }

    @Override
    public void show() {
        super.show();

        gameTimeLabel.setPosition(SCREEN_WIDTH / 2 - (gameTimeLabel.getPrefWidth() / 2), SCREEN_HEIGHT * 0.8f);
        uiStage.addActor(gameTimeLabel);

        infoLabel.setPosition(SCREEN_WIDTH / 2 - (infoLabel.getPrefWidth() / 2), SCREEN_HEIGHT * 0.5f);
        infoLabel.setVisible(false);
        uiStage.addActor(infoLabel);

        scoreLabel.setFontScale(1f);
        scoreLabel.setPosition(0, SCREEN_HEIGHT * 0.5f);
        scoreLabel.setVisible(false);
        uiStage.addActor(scoreLabel);

        Gdx.input.setInputProcessor(actualGame);
    }

    @Override
    public void preUIrender(float delta) {
        if (actualGame.getGameMode() == GameMode.NORMAL_TIME_LIMIT) {
            gameTimeLabel.setText(Tools.formatNumber(actualGame.getGameTimeRemaining(), 2, 2));
        } else if (actualGame.getGameMode() == GameMode.NORMAL_CONTINUOUS) {
            gameTimeLabel.setText(Tools.formatNumber(actualGame.getGameTimeElapsed(), 2, 2));
        }

        actualGame.render(batch);
        drawUI();
    }

    private void drawUI() {
        if (actualGame.getGameState() == Game.GameState.START) {
            if (!infoLabel.isVisible()) infoLabel.setVisible(true);
        } else if (actualGame.getGameState() == Game.GameState.END) {
            //scoreLabel.setText("SCORE:\n" + game.getScore());
            //coreLabel.setVisible(true);
        } else if (actualGame.getGameState() == Game.GameState.RUNNING) {
            if (infoLabel.isVisible()) infoLabel.setVisible(false);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (actualGame.getGameState() == Game.GameState.END) game.setScreen(new ScoreScreen(game, actualGame, skin));
    }
}