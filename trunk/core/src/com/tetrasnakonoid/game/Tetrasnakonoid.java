package com.tetrasnakonoid.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

/* C is better for me than Java. Sorry for this mess. */
/*    		 ---h---
			|   |   |
			w-(x,y)-w
			|   |   |
			 ---h---
		(0,0)
*/
class PointT
{
	int x, y;
	PointT(int xin, int yin) {
		x = xin;
		y = yin;
		flag = 0;
	}
	int flag;
}

class Rect
{
	int x,y,h,w;
}

class Ball {
	int x,y;
	float vx,vy;
	float dx,dy;
	float v;
	float angle;

	long last_col;
	int last_col_x;
	int last_col_y;

	public Ball () {	}
	public Ball(Ball another) {
		this.x = another.x;
		this.y = another.y;
		this.vx = another.vx;
		this.vy = another.vy;
		this.dx= another.vy;
		this.dy = another.vy;
		this.v = another.vy;
		this.angle = another.vy;
	}
}

class Racket {
	float v;
	Rect bb;
	int target_y;
}

class PointF {
	float x; float y;
}

class TetrasnakonoidUsernameDialog implements Input.TextInputListener {
	@Override
	public void input (String text) {
		if (text.length() < 40)
			username = text;
		else
			username = "kek";
	}

	@Override
	public void canceled () {
	}

	public String username = "none";
}

class TetrasnakonoidGame
{
	public static final int max_width_tiles = 40;
	public static final int max_height_tiles = 20;
	public static final int sprite_tile_size_px = 64;
	public int tile_size_px = 64;
	public float k = 1.0f;
	public int vp_h, vp_w;
    public int vp_x, vp_y;

	public static final int racket_length_tiles = 5;
	Rect[] wall;
	Racket pc, ai;
	public static final int racket_speed_tiles_sec = 5;
	Ball ball, lulz_ball;
	int difficulty_a;
	public Color a_color;
	float rack_ai_acc;
    boolean has_lulz_ball;
    boolean pc_turn;

	public static final int snake_init_length_tiles = 3;
	public static final int snake_speed_tiles_per_sec = 5;
	int s_length;
	public static final int total_foods = 16;
	int foodx, foody,food_id;
	int headx, heady;
	public LinkedList<Integer> snake_directions = new LinkedList<Integer>();
	int difficulty_s;
	Color s_color;
	float snake_ani_acc;
	float s_delta;
	public int next_snake_dir = 0;

	public static final int tetris_w_tiles = 10;
	public static final int tetris_h_tiles = 20;
	public static final int tetris_speed_tiles_per_sec = 5;
	int[][] blocks;
	int tetramino_type;
	int tetramino_rot;
	int tetramino_x;
	int tetramino_y;
	float tetris_ani_acc;

	int difficulty_t;
	Color t_color;
	public static final int super_scores = 5;
	int scores;

}
public class Tetrasnakonoid extends ApplicationAdapter implements ApplicationListener {
	public static final boolean DEBUG = false;

	public static final int Answer = 42;
	public static final String version = "v9000.1";
	public static final String corporation = "One Man Company That Makes Everything";

	private ShapeRenderer shapeRenderer;

	private Stage ui, ingame, help, gameover, credits;
	private ScreenViewport viewport;
	private SpriteBatch batch;

	private Texture helpup, helpdown, mutesnd, unmutesnd, mutemusic, unmutemusic, loginup, logindown, ratemeup, ratemedown, donatebtc, donateltc, donateeth, newgame;
	private ImageButton btn_help, btn_mutesnd, btn_mutemusic, btn_login, btn_donate_btc, btn_donate_ltc, btn_donate_eth, btn_rateme, btn_new_game;
	private Drawable drw_helpup, drw_helpdown, drw_mutesnd, drw_unmutesnd, drw_mutemusic, drw_unmutemusic, drw_loginup, drw_logindown,
			drw_ratemeup, drw_ratemedown, drw_donate_btc, drw_donate_ltc, drw_donate_eth, drw_new_game;
	private Label lbl_high_scores_out, lbl_last_try_out, lbl_version;

	private Texture header, protip;
	private Image img_header;
	private Sprite spr_protip;

	private float motto_speed = 500.0f;
	private float motto_x = 0;
	private float motto_y = 0;

	private Drawable drw_header;

	private BitmapFont scorefont;
	private Table layout;

	private Music old_tetris_melody;
	private Sound jingle, fail_jingle, high_score_jingle;
	private long last_jingle = 0;
	private static final long jingle_timeout = 2000;

	private Texture ball, racket;
	private Sprite spr_pc_racket, spr_ai_racket, spr_ball, spr_lulz_ball;
	private TextureRegion ball_frames[];
	private int total_ball_frames = 0;
	private int current_ball_frame = 0;
	private float ball_animation_acc = 0;

	private Texture rack_control;
	private Drawable drw_rack;
	private ImageButton btn_rack;

	private Texture food, head,tail,body,anglebody;
	private Sprite spr_food, spr_head, spr_tail, spr_body, spr_anglebody;

	private Texture snake_control;
	private Drawable drw_snake_ctll, drw_snake_ctlr;
	private ImageButton btn_snakel, btn_snaker;

	private Texture tetrotile, tetroborder;
	private Sprite spr_tetrotile, spr_tetroborderr, spr_tetroborderl;

	private Texture tetr_move, tetr_rot;
	private Drawable drw_tetr_move, drw_tetr_rot;
	private ImageButton btn_tetr_move, btn_tetr_rot;

	private Texture help_background, help_overlay, help_quit;
	private Sprite spr_help_background, spr_help_overlay;
	private Drawable drw_help_quit;
	private ImageButton btn_help_quit;

	TetrasnakonoidUsernameDialog ask_username;

	private Texture sharefb, sharetw, sharevk, sharegh, game_over_quit;
	private TextureRegion game_over_background;
	private Drawable drw_game_over_back, drw_sharefb, drw_sharetw, drw_sharevk, drw_sharegh;
	private ImageButton btn_game_over_back, btn_sharefb, btn_sharetw, btn_sharevk, btn_sharegh;

	private BitmapFont fnt_game_over;
	private Label lbl_scores_final, lbl_quote_final;
	private Table game_over_layout;

	private Label lbl_credits;
	private Texture credits_quit;
	private Drawable drw_credits_quit;
	private ImageButton btn_credits_quit;

	private TetrasnakonoidGame game;

	private static final float[] OLD_TETRIS_COLOR = {0.7529f, 0.8078f, 0.6352f};
	private static final String motto = "protip: you can't";

	private static final String ratemeURI = "http://play.google.com/store/apps/details?id=com.tetrasnakonoid.game";

	private static final String githubURI = "http://github.com/tetrasnakonoid/tetrasnakonoid";
	private static final String facebookURI = "http://facebook.com";
	private static final String twitterURI = "http://twitter.com";
	private static final String vkURI = "http://vk.com";

	private static final String donateBTCURI = "https://raw.githubusercontent.com/tetrasnakonoid/tetrasnakonoid/donations/donateBTC.html";
	private static final String donateLTCURI = "https://raw.githubusercontent.com/tetrasnakonoid/tetrasnakonoid/donations/donateLTC.html";
	private static final String donateETHURI = "https://raw.githubusercontent.com/tetrasnakonoid/tetrasnakonoid/donations/donateETH.html";

	private static final String ETHWallet = "0x7d3935e9b579a53B23d1BC14C23fdDafE9f3d522";
	private static final String BTCWallet = "18gm58hZ7avF18tvhoQmEEkKFTeh2WANoG";
	private static final String LTCWallet = "3HR93M2JPvqBVQrbzETAArUZaq9hpDJfZQ";

	private int game_state = 0;


	private boolean sndmuted = false;
	private boolean musicmuted = true;
	private String player_name = "Anonymous";

	private boolean has_high_scores = false;
	private int high_score = 0;
	private String top_player_name = "Anonymous";
	private int last_score = 0;
	private String last_player_name = "Anonymous";

	private long last_render = 0;
	public boolean game_over_flag = false;
	public boolean first_try = true;
	private float
	todeg(float src) {
		float pi = (float) Math.PI;
		return src * (180.0f / pi);
	}

	private float
	torad(float src) {
		float pi = (float) Math.PI;
		return src * pi / 180.0f;
	}

	private Color getRandomColor() {
		Random rn = new Random();
		float r, g, b ;
		do {
			r = 0.2f + rn.nextFloat() * (0.8f - 0.2f);
			g = 0.2f + rn.nextFloat() * (0.8f - 0.2f);
			b = 0.2f + rn.nextFloat() * (0.8f - 0.2f);
		}
			while (((OLD_TETRIS_COLOR[0] - r) < 0.1f) &&
					((OLD_TETRIS_COLOR[1] - g) < 0.1f) &&
					((OLD_TETRIS_COLOR[2] - b) < 0.1f));

		return new Color(r, g, b, 1);
	}

	private void detect_tile_size() {
		int a = Gdx.graphics.getHeight() / TetrasnakonoidGame.max_height_tiles;
		int b = Gdx.graphics.getWidth() / TetrasnakonoidGame.max_width_tiles;

		game.tile_size_px = Math.min(a, b);
		if (game.tile_size_px % 2 == 1) game.tile_size_px -= 1;

		game.k = (float) game.tile_size_px / (float) TetrasnakonoidGame.sprite_tile_size_px;

		game.vp_h = game.tile_size_px * TetrasnakonoidGame.max_height_tiles;
		game.vp_w = game.tile_size_px * TetrasnakonoidGame.max_width_tiles;

		game.vp_x = (Gdx.graphics.getWidth() - game.vp_w) / 2;
		game.vp_y = (Gdx.graphics.getHeight() - game.vp_h) / 2;
	}

	private void set_random_colors() {
		game.a_color = getRandomColor();
		game.s_color = getRandomColor();
		game.t_color = getRandomColor();
	}

	private void init_pc_racket() {
		spr_pc_racket = new Sprite(racket, TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.racket_length_tiles * TetrasnakonoidGame.sprite_tile_size_px);
		spr_pc_racket.setColor(game.a_color);
		spr_pc_racket.setSize(1.5f*game.k * TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.racket_length_tiles * TetrasnakonoidGame.sprite_tile_size_px * game.k);

		game.pc = new Racket();
		game.pc.bb = new Rect();
		game.pc.v = TetrasnakonoidGame.racket_speed_tiles_sec*game.tile_size_px;

		spr_pc_racket.getHeight();
		game.pc.bb.h = TetrasnakonoidGame.racket_length_tiles * game.tile_size_px / 2;
		game.pc.bb.w = game.tile_size_px / 2;

		game.pc.bb.x = game.tile_size_px;

		int max = game.vp_h - game.pc.bb.h;
		int min = game.pc.bb.h;
		game.pc.bb.y = min + (int) (Math.random() * ((max - min) + 1));
		game.pc.target_y = game.pc.bb.y;

	}

	private void init_ai_racket() {
		spr_ai_racket = new Sprite(racket, TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.racket_length_tiles * TetrasnakonoidGame.sprite_tile_size_px);
		spr_ai_racket.setColor(game.a_color);
		spr_ai_racket.setSize(1.5f*game.k * TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.racket_length_tiles * TetrasnakonoidGame.sprite_tile_size_px * game.k);

		game.ai = new Racket();
		game.ai.bb = new Rect();
		game.ai.v = TetrasnakonoidGame.racket_speed_tiles_sec*game.tile_size_px;

		game.ai.bb.h = TetrasnakonoidGame.racket_length_tiles * game.tile_size_px / 2;
		game.ai.bb.w = game.tile_size_px / 2;

		game.ai.bb.x = game.vp_w - game.tile_size_px;

		int max = game.vp_h - game.ai.bb.h;
		int min = game.ai.bb.h;
		game.ai.bb.y = min + (int) (Math.random() * ((max - min) + 1));
		game.ai.target_y = game.ai.bb.y;

		game.rack_ai_acc = 0.5f;
	}

    private void next_lulz_ball() {
        Random rand = new Random();
        if (rand.nextInt() % 2 == 0) game.lulz_ball.v = 500.0f; else game.lulz_ball.v = -500.0f;

        float angle = 30.0f + (float) (Math.random() * ((150.0f - 30.0f) + 1.0f)) - 90.0f;
        game.lulz_ball.x = game.ball.x;
        game.lulz_ball.y = game.ball.y;
        angle = torad(angle);

        game.lulz_ball.vx = (float) Math.cos(angle) * game.lulz_ball.v;
        game.lulz_ball.vy = (float) Math.sin(angle) * game.lulz_ball.v;
        game.lulz_ball.dx = 0.0f;
        game.lulz_ball.dy = 0.0f;
        game.lulz_ball.angle = angle;
        game.lulz_ball.last_col = TimeUtils.millis();
        game.lulz_ball.last_col_x = -50;
        game.lulz_ball.last_col_y = -50;
        game.has_lulz_ball = true;
    }
	
	private void next_ball() {
		Random rand = new Random();
		game.ball.v = 0.25f * game.vp_w + 0.1f*game.difficulty_a;

		float angle = 30.0f + (float) (Math.random() * ((150.0f - 30.0f) + 1.0f)) - 90.0f;

		if (rand.nextInt() % 2 == 0) {
			game.ball.x = game.ai.bb.x - game.tile_size_px;
			game.ball.y = game.ai.bb.y;
			angle -= 180.0f;
			game.pc_turn = true;
		} else {
			game.ball.x = game.pc.bb.x + game.tile_size_px;
			game.ball.y = game.pc.bb.y;
			game.pc_turn = false;
		}
		angle = torad(angle);

		game.ball.vx = (float) Math.cos(angle) * game.ball.v;
		game.ball.vy = (float) Math.sin(angle) * game.ball.v;
		game.ball.dx = 0.0f;
		game.ball.dy = 0.0f;
		game.ball.angle = angle;
		game.ball.last_col = TimeUtils.millis();
		game.ball.last_col_x = -50;
		game.ball.last_col_y = -50;
	}

	private void init_ball() {
		game.ball = new Ball();
		next_ball();
		spr_ball = new Sprite(ball_frames[0]);
		spr_ball.setOriginCenter();
		spr_ball.setColor(game.a_color);
		spr_ball.setSize(game.k * TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px * game.k);

        spr_lulz_ball = new Sprite(ball_frames[0]);
        spr_lulz_ball.setOriginCenter();
        spr_lulz_ball.setColor(game.a_color);
        spr_lulz_ball.setSize(game.k * TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px * game.k);
	}

	private void init_arcanoid() {
		game.difficulty_a = 0;
		init_pc_racket();
		init_ai_racket();
		init_ball();

		drw_rack = new TextureRegionDrawable(new TextureRegion(rack_control));
		btn_rack = new ImageButton(drw_rack);
		btn_rack.setPosition(0,0);
		btn_rack.setSize(game.vp_x + game.tile_size_px*5, Gdx.graphics.getHeight());
		btn_rack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
			    if (DEBUG) {new_game();}

				int yout = (int) y - game.vp_y;
				int max = game.vp_h - game.pc.bb.h;
				int min = game.pc.bb.h;
				if (yout > max)  yout = max;
				if (yout < min) yout =min;
				game.pc.target_y = yout;
			}
		});
	}
	private void place_walls() {
		game.wall = new Rect[4];
		/* Top */
		int x,y,h,w;
		w=TetrasnakonoidGame.max_width_tiles*game.tile_size_px/2;
		h=TetrasnakonoidGame.max_height_tiles*game.tile_size_px/2;
		x=w;
		y=h;
		game.wall[0] = new Rect();
		game.wall[0].x = x;
		game.wall[0].y = 2*y;
		game.wall[0].h = game.tile_size_px/2;
		game.wall[0].w = w;

		game.wall[1] = new Rect();
		game.wall[1].x = x;
		game.wall[1].y = 0;
		game.wall[1].h = game.tile_size_px/2;
		game.wall[1].w = w;

		game.wall[2] = new Rect();
		game.wall[2].x = 0;
		game.wall[2].y = y;
		game.wall[2].h = h;
		game.wall[2].w = game.tile_size_px/4;

		game.wall[3] = new Rect();
		game.wall[3].x = 2*x;
		game.wall[3].y = y;
		game.wall[3].h = h;
		game.wall[3].w = game.tile_size_px/4;

	}
	private void next_food_xy()
	{
		Random r = new Random();
		int width =(TetrasnakonoidGame.max_width_tiles - TetrasnakonoidGame.tetris_w_tiles)/2 - 2;
		int x = r.nextInt(width);
		if (r.nextInt(2) == 1) game.foodx = 2 + x; else game.foodx = 2 + width + TetrasnakonoidGame.tetris_w_tiles + x;
		game.foody = r.nextInt(TetrasnakonoidGame.max_height_tiles);
	}

	private boolean food_in_snake()
	{
		for (int i=0;i<game.s_length;i++) {
			PointT p = index_to_snake_xy(i);
			if ((p.x == game.foodx) && (p.y == game.foody)) return true;
		}
		return false;
	}

	private void next_food() {
		do {next_food_xy();} while(food_in_snake());

		Random r = new Random();
		game.food_id = r.nextInt(TetrasnakonoidGame.total_foods);

		spr_food.setRegion(TetrasnakonoidGame.sprite_tile_size_px*(game.food_id),0 , TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px);
	}
	private void init_snake(){
		game.difficulty_s = 0;
		game.snake_ani_acc = 0;
		game.s_delta = 0;
		game.s_length = TetrasnakonoidGame.snake_init_length_tiles;
		int index = 0;

		Random r = new Random(); int rd = r.nextInt((4 - 1) + 1) + 1;
		for (int i=0;i<game.s_length;i++) {
			game.snake_directions.add(rd);
		}
		TextureRegion fr = new TextureRegion(food,TetrasnakonoidGame.sprite_tile_size_px*(game.food_id),0 , TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px);
		spr_food = new Sprite(fr);

		next_food_xy(); game.headx = game.foodx;
		game.heady= r.nextInt((TetrasnakonoidGame.max_height_tiles - 0) + 1) + 0;
		next_food();

		spr_food.setColor(game.s_color);
		spr_food.setSize(game.k * TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px * game.k);

		spr_head = new Sprite(head);
		spr_head.setColor(game.s_color);
		spr_head.setSize(game.k * TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px * game.k);
		spr_head.setOriginCenter();

		spr_tail = new Sprite(tail);
		spr_tail.setColor(game.s_color);
		spr_tail.setSize(game.k * TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px * game.k);
		spr_tail.setOriginCenter();

		spr_body = new Sprite(body);
		spr_body.setColor(game.s_color);
		spr_body.setSize(game.k * TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px * game.k);
		spr_body.setOriginCenter();

		spr_anglebody = new Sprite(anglebody);
		spr_anglebody.setColor(game.s_color);
		spr_anglebody.setSize(game.k * TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px * game.k);
		spr_anglebody.setOriginCenter();

		drw_snake_ctll = new TextureRegionDrawable(new TextureRegion(snake_control));
		btn_snakel = new ImageButton(drw_snake_ctll);
		btn_snakel.setPosition(game.vp_x + game.tile_size_px*2,0);
		int scw = (game.vp_w - game.tile_size_px*4 - game.tile_size_px*TetrasnakonoidGame.tetris_w_tiles)/2;
 		btn_snakel.setSize(scw, Gdx.graphics.getHeight());

		btn_snakel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				int orientation = game.snake_directions.getFirst();
				/* Vertical */
				if (y < btn_snakel.getHeight()/2) {
					if ((orientation == 1) || (orientation == 2)) {
						game.next_snake_dir = 3;
					}
				}
				else {
					if (game.tetramino_x == 0) return;
					boolean left = true;
					boolean flag = check_tetramino_move(left);
					if (flag) game.tetramino_x--;
				}



			}
		});

		drw_snake_ctlr = new TextureRegionDrawable(new TextureRegion(snake_control));
		btn_snaker = new ImageButton(drw_snake_ctlr);
		btn_snaker.setPosition(game.vp_x + game.tile_size_px*2+scw+ game.tile_size_px*TetrasnakonoidGame.tetris_w_tiles,0);
		btn_snaker.setSize(scw, Gdx.graphics.getHeight());
		btn_snaker.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				int orientation = game.snake_directions.getFirst();
				/* Vertical */
				if (y < btn_snaker.getHeight()/2) {
					if ((orientation == 1) || (orientation == 2)) {
						game.next_snake_dir = 4;
					}
				}
				else {
					int width = get_tetramino_width(game.tetramino_type, game.tetramino_rot);
					if (width + game.tetramino_x > TetrasnakonoidGame.tetris_w_tiles - 1) return;

					boolean flag = check_tetramino_move(false);
					if ((flag))
							game.tetramino_x++;
				}

			}
		});
	}

	private void tetramino_to_block() {
		for (int i = 0;i < TetrasnakonoidGame.tetris_h_tiles + 4 ; ++i)
			for (int j = 0; j < TetrasnakonoidGame.tetris_w_tiles ; ++j) {
				if (game.blocks[i][j] == 2) game.blocks[i][j] = 1;
			}
	}

	private void clear_tetramino() {
			for (int i = 0; i < TetrasnakonoidGame.tetris_h_tiles + 4; ++i)
				for (int j = 0; j < TetrasnakonoidGame.tetris_w_tiles; ++j){
				if (game.blocks[i][j] == 2) game.blocks[i][j] = 0;
			}
	}

	private void unput_tetramino() {
		for (int i = 0; i < TetrasnakonoidGame.tetris_h_tiles + 4; ++i)
			for (int j = 0; j < TetrasnakonoidGame.tetris_w_tiles; ++j){
				if ((game.blocks[i][j] == 2) || (game.blocks[i][j] ==3) || (game.blocks[i][j] ==4)) game.blocks[i][j] -= 2;
			}
	}
	private void
	put_tetramino(int type, int rot, int x, int y)
	{

		switch (type)
		{
			case 0: {
				game.blocks[y][x] += 2;
				game.blocks[y][++x] += 2;
				game.blocks[++y][x] += 2;
				game.blocks[y][--x] += 2;
				break;
				}
			case 1: {
				if (rot == 0) {
					game.blocks[y][x] += 2;
					game.blocks[y][++x] += 2;
					game.blocks[y][++x] += 2;
					game.blocks[y][++x] += 2;
				}
				else {
					game.blocks[y][x] += 2;
					game.blocks[++y][x] += 2;
					game.blocks[++y][x] += 2;
					game.blocks[++y][x] += 2;
				}
				break;
			}

			case 2: {
				if (rot == 0) {
					game.blocks[++y][x] += 2;
					game.blocks[y][++x] += 2;
					game.blocks[--y][x] += 2;
					game.blocks[y][++x] += 2;
				}
				else {
					game.blocks[y][x] += 2;
					game.blocks[++y][x] += 2;
					game.blocks[y][++x] += 2;
					game.blocks[++y][x] += 2;
				}
				break;
			}

			case 3:
			{
				if (rot == 0) {
					game.blocks[y][x] += 2;
					game.blocks[y][++x] += 2;
					game.blocks[++y][x] += 2;
					game.blocks[y][++x] += 2;
				}
				else {
					game.blocks[y][++x] += 2;
					game.blocks[++y][x] += 2;
					game.blocks[y][--x] += 2;
					game.blocks[++y][x] += 2;
				}
				break;
			}
			case 4:
			{
				if (rot == 0) {
					game.blocks[++y][x] += 2;
					game.blocks[y][++x] += 2;
					game.blocks[y][++x] += 2;
					game.blocks[--y][--x] += 2;
				}
				if (rot == 1)  {
					game.blocks[y][++x] += 2;
					game.blocks[++y][x] += 2;
					game.blocks[++y][x] += 2;
					game.blocks[--y][--x] += 2;
				}
				if (rot == 2)  {
					game.blocks[y][x] += 2;
					game.blocks[y][++x] += 2;
					game.blocks[y][++x] += 2;
					game.blocks[++y][--x] += 2;
				}
				if (rot == 3)  {
					game.blocks[y][x] += 2;
					game.blocks[++y][x] += 2;
					game.blocks[++y][x] += 2;
					game.blocks[--y][++x] += 2;
				}
				break;
			}
			case 5:
			{
				if (rot == 0) {
					game.blocks[y][x] += 2;
					game.blocks[++y][x] += 2;
					game.blocks[++y][x] += 2;
					game.blocks[y][++x] += 2;
				}
				if (rot == 1)  {
					game.blocks[++y][x] += 2;
					game.blocks[y][++x] += 2;
					game.blocks[y][++x] += 2;
					game.blocks[--y][x] += 2;
				}
				if (rot == 2)  {
					game.blocks[y][x] += 2;
					game.blocks[y][++x] += 2;
					game.blocks[++y][x] += 2;
					game.blocks[++y][x] += 2;
				}
				if (rot == 3)  {
					game.blocks[y][x] += 2;
					game.blocks[++y][x] += 2;
					game.blocks[--y][++x] += 2;
					game.blocks[y][++x] += 2;
				}
				break;
			}
			case 6:
				{
					if (rot == 0) {
						game.blocks[y][x] += 2;
						game.blocks[y][++x] += 2;
						game.blocks[++y][--x] += 2;
						game.blocks[++y][x] += 2;
					}
					if (rot == 1)  {
						game.blocks[y][x] += 2;
						game.blocks[++y][x] += 2;
						game.blocks[y][++x] += 2;
						game.blocks[y][++x] += 2;
					}
					if (rot == 2)  {
						game.blocks[y][x] += 2;
						game.blocks[++y][x] += 2;
						game.blocks[++y][x] += 2;
						game.blocks[y][--x] += 2;
					}
					if (rot == 3)  {
						game.blocks[y][x] += 2;
						game.blocks[y][++x] += 2;
						game.blocks[y][++x] += 2;
						game.blocks[++y][x] += 2;
					}
					break;
				}
			default: break;
		}
	}

	private int
	get_tetramino_height(int type, int rot)
	{
		switch (type)
		{
			case 0: return 2;
			case 1: if (rot == 0) return 1; else return 4;
			case 2: if (rot == 0) return 2; else return 3;
			case 3: if (rot == 0) return 2; else return 3;

			case 4: if ((rot == 0) || (rot ==2)) return 2; else return 3;
			case 5: if ((rot == 0) || (rot ==2)) return 3; else return 2;
			case 6: if ((rot == 0) || (rot ==2)) return 3; else return 2;
			default: break;
		}
		return 0;
	}

	private int
	get_tetramino_width(int type, int rot)
	{
		switch (type)
		{
			case 0: return 2;
			case 1: if (rot == 0) return 4; else return 1;
			case 2: if (rot == 0) return 3; else return 2;
			case 3: if (rot == 0) return 3; else return 2;

			case 4: if ((rot == 0) || (rot ==2)) return 3; else return 2;
			case 5: if ((rot == 0) || (rot ==2)) return 2; else return 3;
			case 6: if ((rot == 0) || (rot ==2)) return 2; else return 3;
			default: break;
		}
		return 0;
	}

private void next_tetr_rot() {
	switch (game.tetramino_type)
	{
		case 0: break;
		case 1: if (game.tetramino_rot >=1) game.tetramino_rot=0; else game.tetramino_rot++; break;
		case 2: if (game.tetramino_rot >=1) game.tetramino_rot=0; else game.tetramino_rot++; break;
		case 3:  if (game.tetramino_rot>=1) game.tetramino_rot=0; else game.tetramino_rot++; break;

		case 4:  if (game.tetramino_rot >=3) game.tetramino_rot=0; else game.tetramino_rot++; break;
		case 5:  if (game.tetramino_rot >=3) game.tetramino_rot=0; else game.tetramino_rot++; break;
		case 6:  if (game.tetramino_rot >=3) game.tetramino_rot=0; else game.tetramino_rot++; break;
		default: break;
	}
}

private void prev_tetr_rot() {
		switch (game.tetramino_type)
		{
			case 0: break;
			case 1: if (game.tetramino_rot ==0) game.tetramino_rot=1; else game.tetramino_rot--; break;
			case 2: if (game.tetramino_rot ==0) game.tetramino_rot=1; else game.tetramino_rot--; break;
			case 3:  if (game.tetramino_rot ==0) game.tetramino_rot=1; else game.tetramino_rot--; break;

			case 4:  if (game.tetramino_rot ==0) game.tetramino_rot=3; else game.tetramino_rot--; break;
			case 5:  if (game.tetramino_rot ==0) game.tetramino_rot=3; else game.tetramino_rot--; break;
			case 6:  if (game.tetramino_rot ==0) game.tetramino_rot=3; else game.tetramino_rot--; break;
			default: break;
		}
	}

	private void next_tetramino() {
		Random r = new Random();
		game.tetramino_type = r.nextInt(7);
		switch (game.tetramino_type)
		{
			case 0: game.tetramino_rot = 0;  break; /* :: */
			case 1: game.tetramino_rot = r.nextInt(2); break; /* **** */
			case 2: game.tetramino_rot = r.nextInt(2); break; /* Z left */
			case 3: game.tetramino_rot = r.nextInt(2); break; /* z right */
			case 4: game.tetramino_rot = r.nextInt(4); break; /* T */
			case 5: game.tetramino_rot = r.nextInt(4); break; /* L */
			case 6: game.tetramino_rot = r.nextInt(4); break; /* reverse L */
			default: break;
		}
		game.tetramino_y = TetrasnakonoidGame.tetris_h_tiles-1;
		game.tetramino_x = (TetrasnakonoidGame.tetris_w_tiles - get_tetramino_width(game.tetramino_type, game.tetramino_rot))/2;
		put_tetramino(game.tetramino_type, game.tetramino_rot, game.tetramino_x, game.tetramino_y);
	}
	   private void init_tetris(){
		game.difficulty_t = 0;
		game.tetris_ani_acc  = 0;

		game.blocks = new int[TetrasnakonoidGame.tetris_h_tiles+4][TetrasnakonoidGame.tetris_w_tiles+4];
		for (int i =0; i < TetrasnakonoidGame.tetris_h_tiles+4; ++i)
			for(int j =0;j<TetrasnakonoidGame.tetris_w_tiles;++j){
					game.blocks[i][j]=0;
			}
		next_tetramino();

		spr_tetrotile = new Sprite(tetrotile);
		spr_tetrotile.setColor(game.t_color);
		spr_tetrotile.setSize(game.k * TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px * game.k);
		spr_tetrotile.setOriginCenter();

		spr_tetroborderl = new Sprite(tetroborder);
		spr_tetroborderl.setColor(game.t_color);
		spr_tetroborderl.setSize(5, game.vp_h);
		int lbx = (TetrasnakonoidGame.max_width_tiles - TetrasnakonoidGame.tetris_w_tiles)/2;
		spr_tetroborderl.setPosition(lbx* game.tile_size_px + game.vp_x, game.vp_y);

		spr_tetroborderr = new Sprite(tetroborder);
		spr_tetroborderr.setColor(game.t_color);
		spr_tetroborderr.setSize(5, game.vp_h);
		spr_tetroborderr.setPosition((lbx + TetrasnakonoidGame.tetris_w_tiles)  * game.tile_size_px + game.vp_x, game.vp_y);


		drw_tetr_move = new TextureRegionDrawable(new TextureRegion(tetr_move));
		btn_tetr_move = new ImageButton(drw_tetr_move);
		btn_tetr_move.setPosition(lbx* game.tile_size_px + game.vp_x,0);
		btn_tetr_move.setSize(TetrasnakonoidGame.tetris_w_tiles*game.tile_size_px, Gdx.graphics.getHeight());
		btn_tetr_move.addListener(new ClickListener() {
			   @Override
			   public void clicked(InputEvent event, float x, float y) {
				   	int orientation = game.snake_directions.getFirst();
			   		if (y<btn_tetr_move.getHeight()/2) {
						if ((orientation == 3) || (orientation == 4)) {
							game.next_snake_dir =1;
						}
					}
					else {
						if ((orientation == 3) || (orientation == 4)) {
							game.next_snake_dir = 2;
						}
					}
			   }});

		drw_tetr_rot = new TextureRegionDrawable(new TextureRegion(tetr_rot));
		btn_tetr_rot = new ImageButton(drw_tetr_rot);


		btn_tetr_rot.setPosition( Gdx.graphics.getWidth() - game.vp_x - game.tile_size_px*5,0);
		btn_tetr_rot.setSize(game.vp_x + game.tile_size_px*5, Gdx.graphics.getHeight());
		btn_tetr_rot.addListener(new ClickListener() {
			   @Override
			   public void clicked(InputEvent event, float x, float y) {
					if (check_tetramino_rot()) next_tetr_rot();
			   }});
	}

	private void save_cookies()
	{
		FileHandle scores = Gdx.files.local("scores");
		FileHandle config = Gdx.files.local("config");

		String out = player_name;

		last_player_name = player_name;
		last_score = game.scores;

		String lbl = "LAST: ";
		lbl+= last_player_name;
		lbl+= " ";
		lbl+= String.valueOf(last_score);
		lbl_last_try_out.setText(lbl);

		if (game.scores > high_score) {
			top_player_name = player_name;
			high_score = game.scores;

			out+="\n";
			out+=String.valueOf(game.scores);
			scores.writeString(out,false);

			lbl = "TOP : ";
			lbl+= last_player_name;
			lbl+= " ";
			lbl+= String.valueOf(last_score);
			lbl_last_try_out.setText(lbl);
			lbl_high_scores_out.setText(lbl);
		}


		String out2 = player_name;
		out2+="\n";
		if  (sndmuted) out2 +=String.valueOf(0); else out2 +=String.valueOf(1);
		out2+="\n";
		if  (musicmuted) out2 +=String.valueOf(0); else out2 +=String.valueOf(1);
		config.writeString(out2,false);

	}

	private String get_random_quote(int scores) {
		int total_quotes = 10;
		final String[] quotes = new String[total_quotes];
		quotes[0] = "IDDQD";
		quotes[1] = "Holy shit";
		quotes[2] = "Rampage";
		quotes[3] = "Let's rock! ";
		quotes[4] = "Come Get Some";
		quotes[5] = "mmMMMMMONSTERKILLKILLKILL";
		quotes[6] = "RIP AND TEAR";
		quotes[7] = "Impossible";
		quotes[8] = "Unbelievable";
		quotes[9] = "Are you a jet fighter pilot?";


		final String[] newfag_quotes = new String[total_quotes];
		newfag_quotes[0] = motto;
		newfag_quotes[1] = "Rank: Puppy";
		newfag_quotes[2] = "Rank: Kitten";
		newfag_quotes[3] = "Rank: Newbie";
		newfag_quotes[4] = "Rank: Slowpoke";
		newfag_quotes[5] = "Try harder";
		newfag_quotes[6] = "No, John. You are the Tetrasnakonoid.";
		newfag_quotes[7] = "AHAHAHAHAHAAHAHAH";
		newfag_quotes[8] = "You Are Already Dead.";
		newfag_quotes[9] = "All your base are belong to us.";

		Random r = new Random();
		int id = r.nextInt(total_quotes);

		if (scores>300) return quotes[id]; else return newfag_quotes[id];
	}


	private void game_over(){
		if (DEBUG) {new_game();}
		first_try = false;
		game_state = 4;
		Gdx.input.setInputProcessor(gameover);
		String scores = String.valueOf(game.scores);
		lbl_scores_final.setText(scores);
		lbl_quote_final.setText(get_random_quote(game.scores));
		if (!sndmuted) {
			if (game.scores > high_score) high_score_jingle.play(1.0f);
			else
				fail_jingle.play(1.0f);
		}

		save_cookies();


		game_over_flag = true;


	}
	private void new_game() {
		if (DEBUG) {
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
			shapeRenderer = new ShapeRenderer();
		}
		game_state = 1;
		game = new TetrasnakonoidGame();
		game.scores = 0;
		game_over_flag = false;

		detect_tile_size();
		set_random_colors();
		place_walls();
		init_arcanoid();
		init_snake();
		init_tetris();

		ingame = new Stage(viewport);

		ingame.addActor(btn_snakel);
		ingame.addActor(btn_snaker);

		ingame.addActor(btn_tetr_move);
		ingame.addActor(btn_tetr_rot);
		ingame.addActor(btn_rack);
		/*
		ingame.addActor(btn_new_game);
		btn_new_game.setPosition(Gdx.graphics.getWidth() - (game.vp_x + game.tile_size_px*2),0);
		btn_new_game.setSize(game.vp_x + game.tile_size_px*2, Gdx.graphics.getHeight());*/
		game.lulz_ball = new Ball();
		game.has_lulz_ball = false;
		Gdx.input.setInputProcessor(ingame);
		last_render = TimeUtils.millis();
	}

	private void init_music() {
		old_tetris_melody = Gdx.audio.newMusic(Gdx.files.internal("tetris.mp3"));
		old_tetris_melody.setLooping(true);
		old_tetris_melody.setVolume(0.3f);
		jingle = Gdx.audio.newSound(Gdx.files.internal("jingle.wav"));
		high_score_jingle = Gdx.audio.newSound(Gdx.files.internal("highscores.wav"));
		fail_jingle = Gdx.audio.newSound(Gdx.files.internal("fail.wav"));
		if (!musicmuted) {
			old_tetris_melody.play();
		}
	}
	private void init_helpscreen(){
		spr_help_background = new Sprite(help_background);
		spr_help_background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spr_help_overlay = new Sprite(help_overlay);
		spr_help_background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		drw_help_quit = new TextureRegionDrawable(new TextureRegion(help_quit));

		btn_help_quit = new ImageButton(drw_help_quit);
		btn_help_quit.setPosition(0,0);
		btn_help_quit.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		help = new Stage(viewport);
		help.addActor(btn_help_quit);
		btn_help_quit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game_state = 0;
				Gdx.input.setInputProcessor(ui);
			}
		});
	}

	private void init_gameoverscreen(){
		game_over_background = new TextureRegion();

		/* fnt_game_over;
		lbl_scores_final;
		game_over_layout;*/

		drw_game_over_back = new TextureRegionDrawable(new TextureRegion(game_over_quit));
		btn_game_over_back = new ImageButton(drw_game_over_back);
		btn_game_over_back.setPosition(0,0);
		btn_game_over_back.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		drw_sharefb = new TextureRegionDrawable(new TextureRegion(sharefb));
		btn_sharefb = new ImageButton(drw_sharefb);

		drw_sharetw = new TextureRegionDrawable(new TextureRegion(sharetw));
		btn_sharetw = new ImageButton(drw_sharetw);

		drw_sharevk = new TextureRegionDrawable(new TextureRegion(sharevk));
		btn_sharevk = new ImageButton(drw_sharevk);

		drw_sharegh = new TextureRegionDrawable(new TextureRegion(sharegh));
		btn_sharegh = new ImageButton(drw_sharegh);

		fnt_game_over = new BitmapFont(Gdx.files.internal("IMPACT-144.fnt"));
		scorefont.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		Label.LabelStyle game_over_scores_style = new Label.LabelStyle();
		game_over_scores_style.font = fnt_game_over;
		game_over_scores_style.fontColor = Color.WHITE;
		lbl_scores_final = new Label("0", game_over_scores_style);
		lbl_scores_final.scaleBy(1.5f);

		Label.LabelStyle quote_style = new Label.LabelStyle();
		quote_style.font = scorefont;
		quote_style.fontColor = Color.BLACK;
		lbl_quote_final = new Label("Rank: Slowpoke", quote_style);
		lbl_quote_final.scaleBy(1.5f);

		TextureRegionDrawable drw_logo = new TextureRegionDrawable(new TextureRegion(header));
		img_header = new Image(drw_logo);
		Image logo = new Image(header);

		gameover = new Stage(viewport);
		game_over_layout = new Table();
		game_over_layout.setFillParent(true);
		game_over_layout.add(logo).center().colspan(4).pad(10).spaceBottom(100);
		game_over_layout.row();
		game_over_layout.add(lbl_scores_final).center().colspan(4).pad(10).spaceBottom(100);
		game_over_layout.row();
		game_over_layout.add(lbl_quote_final).center().colspan(4).pad(10).spaceBottom(100);
		game_over_layout.row();
		game_over_layout.add(btn_sharefb).pad(10);
		game_over_layout.add(btn_sharetw).pad(10);
		game_over_layout.add(btn_sharevk).pad(10);
		game_over_layout.add(btn_sharegh).pad(10);

		gameover.addActor(btn_game_over_back);
		gameover.addActor(game_over_layout);

		btn_game_over_back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game_state = 0;
				Gdx.input.setInputProcessor(ui);
			}
		});

		btn_sharefb.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.net.openURI(facebookURI);
			}
		});

		btn_sharetw.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.net.openURI(twitterURI);
			}
		});

		btn_sharevk.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.net.openURI(vkURI);
			}

		});

		btn_sharegh.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.net.openURI(githubURI);
			}

		});

	}
	private void init_creditsscreen() {
		String str_credits = "Created from scratch for fun and profit\n" +
				             "By One Man Company That Makes Everything.\n" +
						"Totally Free and BSD 2-clause Open Source. No ADS.\n" +
				              "Thanks to libGDX, Inkscape, Audacity, Paint .NET\n" +
				         "And also any contributor which sprite sound or font was used\n" +
		                 "Consider a small cryptocurrency beer donation\n" +
						 "So I can haz motivation to polish it further. See ya. \n" +
						 "P.S. Wallets for donataions:\n" +
						  BTCWallet + "\n" +
				          LTCWallet + "\n" +
				          ETHWallet;

		Label.LabelStyle credits_style = new Label.LabelStyle();
		credits_style.font = scorefont;
		credits_style.fontColor = Color.BLACK;
		lbl_credits = new Label(str_credits, credits_style);

		drw_credits_quit = new TextureRegionDrawable(new TextureRegion(credits_quit));
		btn_credits_quit = new ImageButton(drw_credits_quit);
		btn_credits_quit.setTransform(true);
		btn_credits_quit.setScale(256.0f);


		credits = new Stage(viewport);
		Table credits_layout = new Table();
		credits_layout.setFillParent(true);
		credits_layout.add(lbl_credits).center().colspan(1).pad(10);


		credits.addActor(credits_layout);
		credits.addActor(btn_credits_quit);

		btn_credits_quit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game_state = 0;
				Gdx.input.setInputProcessor(ui);
			}
		});
	}

	private void init_gui() {


		drw_helpup = new TextureRegionDrawable(new TextureRegion(helpup));
		drw_helpdown = new TextureRegionDrawable(new TextureRegion(helpdown));
		btn_help = new ImageButton(drw_helpup, drw_helpdown);

		drw_mutesnd = new TextureRegionDrawable(new TextureRegion(mutesnd));
		drw_unmutesnd = new TextureRegionDrawable(new TextureRegion(unmutesnd));
		btn_mutesnd = new ImageButton(drw_mutesnd, drw_mutesnd, drw_unmutesnd);
		btn_mutesnd.setChecked(sndmuted);

		drw_mutemusic = new TextureRegionDrawable(new TextureRegion(mutemusic));
		drw_unmutemusic = new TextureRegionDrawable(new TextureRegion(unmutemusic));
		btn_mutemusic = new ImageButton(drw_mutemusic, drw_mutemusic, drw_unmutemusic);
		btn_mutemusic.setChecked(musicmuted);

		drw_loginup = new TextureRegionDrawable(new TextureRegion(loginup));
		drw_logindown = new TextureRegionDrawable(new TextureRegion(logindown));
		btn_login = new ImageButton(drw_loginup, drw_logindown);

		drw_donate_btc = new TextureRegionDrawable(new TextureRegion(donatebtc));
		btn_donate_btc = new ImageButton(drw_donate_btc);

		drw_donate_ltc = new TextureRegionDrawable(new TextureRegion(donateltc));
		btn_donate_ltc = new ImageButton(drw_donate_ltc);

		drw_donate_eth = new TextureRegionDrawable(new TextureRegion(donateeth));
		btn_donate_eth = new ImageButton(drw_donate_eth);

		drw_ratemeup = new TextureRegionDrawable(new TextureRegion(ratemeup));
		drw_ratemedown = new TextureRegionDrawable(new TextureRegion(ratemedown));
		btn_rateme = new ImageButton(drw_ratemeup, drw_ratemedown);

		drw_new_game = new TextureRegionDrawable(new TextureRegion(newgame));
		btn_new_game = new ImageButton(drw_new_game);

		drw_header = new TextureRegionDrawable(new TextureRegion(header));
		img_header = new Image(drw_header);

		spr_protip = new Sprite(protip, 0, 0, 426, 56);
		motto_y = 60 + (float) Math.random() * (Gdx.graphics.getHeight() - 2 * 60);


		scorefont = new BitmapFont(Gdx.files.internal("Inconsolata-LGC-48.fnt"));
		scorefont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		Label.LabelStyle score_style = new Label.LabelStyle();
		score_style.font = scorefont;
		score_style.fontColor = Color.BLACK;


		String lbl ="TOP : ";
		if (has_high_scores) {
			lbl += top_player_name;
			lbl += " ";
			lbl += String.valueOf(high_score);

		}
		else {
			lbl += "Anonymous 0";
		}
		lbl_high_scores_out = new Label(lbl, score_style);

		lbl = "LAST: ";
		if (!first_try) {
			lbl+= last_player_name;
			lbl+= " ";
			lbl+= String.valueOf(last_score);
		}
		else {
			lbl = "Tap to start...";
		}
		lbl_last_try_out = new Label(lbl, score_style);

		lbl_version = new Label(version, score_style);
		lbl_version.setPosition(0,0);

		viewport = new ScreenViewport();
		init_helpscreen();
		init_gameoverscreen();
		init_creditsscreen();
		ask_username = new TetrasnakonoidUsernameDialog();
		ui = new Stage(viewport);

		layout = new Table();
		layout.setFillParent(true);
		layout.add(img_header).center().colspan(8).pad(10).spaceBottom(100);
		layout.row();
		layout.add(lbl_high_scores_out).center().colspan(8).pad(10);
		layout.row();
		layout.add(lbl_last_try_out).center().colspan(8).pad(10).spaceBottom(100);
		layout.row();
		layout.add(btn_help).pad(10);
		layout.add(btn_mutesnd).pad(10);
		layout.add(btn_mutemusic).pad(10);
		layout.add(btn_login).pad(10).spaceRight(300);


		layout.add(btn_rateme).pad(10);
		layout.add(btn_donate_btc).pad(10);
		layout.add(btn_donate_ltc).pad(10);
		layout.add(btn_donate_eth).pad(10);


		btn_new_game.setTransform(true);
		btn_new_game.setScale(256.0f);
		ui.addActor(btn_new_game);
		ui.addActor(layout);
		ui.addActor(lbl_version);
		Gdx.input.setInputProcessor(ui);

		btn_help.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game_state = 2;
				Gdx.input.setInputProcessor(help);
			}
		});

		btn_mutesnd.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				sndmuted = !sndmuted;
			}
		});

		btn_mutemusic.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				musicmuted = !musicmuted;

				if (musicmuted)
					old_tetris_melody.stop();
				else
					old_tetris_melody.play();
			}
		});



		btn_login.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.input.getTextInput(ask_username, "Name yourself", "Stranger", "Anonymous");
			}
		});

		btn_donate_btc.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.net.openURI(donateBTCURI);

			}
		});

		btn_donate_ltc.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.net.openURI(donateLTCURI);
			}
		});

		btn_donate_eth.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.net.openURI(donateETHURI);
			}
		});


		btn_rateme.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.net.openURI(ratemeURI);

			}
		});

		lbl_version.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.input.setInputProcessor(credits);
				game_state = 3;
			}
		});

		btn_new_game.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!ask_username.username.equals("none")) player_name = ask_username.username;
				new_game();


			}
		});

		img_header.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				new_game();
			}
		});
	}

	private void init_animations() {
		int ball_cols = 12;
		int ball_rows = 1;

		ball_frames = new TextureRegion[ball_cols * ball_rows];
		for (int i = 0; i < ball_rows*ball_rows; i++) {
				ball_frames[i] =new TextureRegion(ball,TetrasnakonoidGame.sprite_tile_size_px*i, 0, TetrasnakonoidGame.sprite_tile_size_px,TetrasnakonoidGame.sprite_tile_size_px);

		}

		total_ball_frames = ball_cols * ball_rows;
	}

	private void init_textures() {
		/* Hello, China! */
		helpup = new Texture(Gdx.files.internal("helpup.png"));
		helpdown = new Texture(Gdx.files.internal("helpdown.png"));
		mutesnd = new Texture(Gdx.files.internal("mutesound.png"));
		unmutesnd = new Texture(Gdx.files.internal("unmutesound.png"));
		mutemusic = new Texture(Gdx.files.internal("mutemusic.png"));
		unmutemusic = new Texture(Gdx.files.internal("unmutemusic.png"));
		loginup = new Texture(Gdx.files.internal("loginup.png"));
		logindown = new Texture(Gdx.files.internal("logindown.png"));
		ratemeup = new Texture(Gdx.files.internal("ratemeup.png"));
		ratemedown = new Texture(Gdx.files.internal("ratemedown.png"));
		donatebtc = new Texture(Gdx.files.internal("donatebtc.png"));
		donateltc = new Texture(Gdx.files.internal("donateltc.png"));
		donateeth = new Texture(Gdx.files.internal("donateeth.png"));
		header = new Texture(Gdx.files.internal("header.png"));
		protip = new Texture(Gdx.files.internal("motto.png"));
		newgame = new Texture(Gdx.files.internal("newgame.png"));

		ball = new Texture(Gdx.files.internal("ball.png"));
		racket = new Texture(Gdx.files.internal("racket.png"));
		rack_control = new Texture(Gdx.files.internal("rack_control.png"));

		food = new  Texture(Gdx.files.internal("food.png"));
		head = new  Texture(Gdx.files.internal("head.png"));
		tail = new  Texture(Gdx.files.internal("tail.png"));
		body = new  Texture(Gdx.files.internal("body.png"));
		anglebody = new  Texture(Gdx.files.internal("body_angle.png"));
		snake_control = new  Texture(Gdx.files.internal("snake_control.png"));

		tetrotile = new Texture(Gdx.files.internal("tetrotile.png"));
		tetroborder = new Texture(Gdx.files.internal("border.png"));
		tetr_move  = new Texture("tetr_move.png");
		tetr_rot = new Texture("tetr_rot.png");

		help_background = new Texture("help_background.png");
		help_overlay = new Texture("help_overlay.png");
		help_quit = new Texture("help_quit.png");

		sharefb = new Texture("sharefb.png");
		sharetw = new Texture("sharetw.png");
		sharevk = new Texture("sharevk.png");
		sharegh = new Texture("sharegh.png");
		game_over_quit = new Texture("gameover_quit.png");

		credits_quit = new Texture("credits_quit.png");
	}

	private void init_cookies()
	{
		FileHandle scores = Gdx.files.local("scores");
		FileHandle config = Gdx.files.local("config");

		if (scores.exists()) {
			String str_scores = scores.readString();
			String out[] = str_scores.split("\\r?\\n");

			top_player_name = out[0];
			high_score = Integer.parseInt(out[1]);

			has_high_scores = true;
		}
		if (config.exists()) {
			String str_config = config.readString();
			String out[] = str_config.split("\\r?\\n");

			player_name = out[0];
			int sndm = Integer.parseInt(out[1]);
			int mscm = Integer.parseInt(out[2]);

			if (sndm == 1) sndmuted = false; else sndmuted = true;
			if (mscm == 1) musicmuted = false; else musicmuted = true;
		}
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		init_textures();
		init_music();
		init_cookies();
		init_gui();
		init_animations();


	}

	private void render_gui() {
		motto_x += Gdx.graphics.getDeltaTime() * motto_speed;
		if (motto_x > Gdx.graphics.getWidth()) {
			motto_x = 0;
			motto_y = 60 + (float) Math.random() * (Gdx.graphics.getHeight() - 2 * 60);
		}

		ui.draw();
		batch.begin();
		batch.draw(spr_protip, (int) motto_x, (int) motto_y);
		batch.end();


	}

	private void rect_to_sprite(Sprite dest, Rect r) {
		dest.setPosition(r.x - r.w + game.vp_x, r.y - r.h + game.vp_y);


	}

	private Rect
	ball_to_rect(Ball ball) {
		Rect r = new Rect();
		r.x = ball.x;
		r.y = ball.y;
		r.h = game.tile_size_px / 2;
		r.w = game.tile_size_px / 2;
		return r;
	}

	private void update_sprites() {

		rect_to_sprite(spr_pc_racket, game.pc.bb);
		rect_to_sprite(spr_ai_racket, game.ai.bb);
		rect_to_sprite(spr_ball, ball_to_rect(game.ball));
		if (game.has_lulz_ball) rect_to_sprite(spr_lulz_ball, ball_to_rect(game.lulz_ball));

		ball_animation_acc += Gdx.graphics.getDeltaTime();

		if (ball_animation_acc> 0.1) {
			current_ball_frame++; if (current_ball_frame == total_ball_frames) current_ball_frame = 0;
			ball_animation_acc = 0;
			spr_ball.setRegion(new TextureRegion(ball,TetrasnakonoidGame.sprite_tile_size_px*current_ball_frame, 0, TetrasnakonoidGame.sprite_tile_size_px,TetrasnakonoidGame.sprite_tile_size_px));
		}


	}

	private void animate_ball() {
		float dt = Gdx.graphics.getDeltaTime();
		game.ball.dx += game.ball.vx * dt;
		game.ball.dy += game.ball.vy * dt;

		int dx = (int) Math.ceil(game.ball.dx);
		int dy = (int) Math.ceil(game.ball.dy);

		if (dx != 0) {
			game.ball.x += dx;
			game.ball.dx = 0.0f;
		}
		if (dy != 0) {
			game.ball.y += dy;
			game.ball.dy = 0.0f;
		}
	}

    private void animate_lulz_ball() {
        float dt = Gdx.graphics.getDeltaTime();
        game.lulz_ball.dx += game.lulz_ball.vx * dt;
        game.lulz_ball.dy += game.lulz_ball.vy * dt;

        int dx = (int) Math.ceil(game.lulz_ball.dx);
        int dy = (int) Math.ceil(game.lulz_ball.dy);

        if (dx != 0) {
            game.lulz_ball.x += dx;
            game.lulz_ball.dx = 0.0f;
        }
        if (dy != 0) {
            game.lulz_ball.y += dy;
            game.lulz_ball.dy = 0.0f;
        }
        if (    (game.lulz_ball.x > Gdx.graphics.getWidth()) ||
                (game.lulz_ball.x < 0) ||
                (game.lulz_ball.y > Gdx.graphics.getHeight()) ||
                (game.lulz_ball.y < 0)) {
            game.has_lulz_ball = false;
        }
    }
	
	private boolean
	rect_rect(Rect rect1, Rect rect2)
	{
		return ((rect1.x - rect1.w) < (rect2.x + rect2.w) && (rect1.x + rect1.w) > (rect2.x - rect2.w) &&
				(rect1.y - rect1.h) < (rect2.y + rect2.h) && (rect1.y + rect1.h) > (rect2.y - rect2.h));
	}

	private boolean
	rect_circle(Rect rect, Rect circle_b)
	{
		int r = game.tile_size_px/2;
		int cdistx = Math.abs(circle_b.x - rect.x);
		int cdisty = Math.abs(circle_b.y - rect.y);

		if (cdistx >= (rect.w + r)) return false;
		if (cdisty >= (rect.h + r)) return false;

		if (cdistx <= (rect.w))  return true;
		if (cdisty <= (rect.h))  return true;

		int corn_dist = (cdistx - rect.w) * (cdistx - rect.w) +
				(cdisty - rect.h) * (cdisty - rect.h);

		return (corn_dist <= r*r);
	}

	private float
	get_angle(float x1, float y1, float x2, float y2)
	{
		return (float) Math.atan2(x1*x2 + y1*y2, x1*y2 - y1*x2);
	}


	private void
	set_angle(PointF dest, PointF src, float angle)
	{
    	dest.x = (float) ((src.x) * Math.cos(angle) - (src.y) * Math.sin(angle));
    	dest.y = (float) ((src.y) * Math.cos(angle) + (src.x) * Math.sin(angle));
	}

	private int
	detect_wall_orientation(Ball ball, Rect rect)
	{
		/* 1 - horizontal */
		Ball test_ball = new Ball(ball);

		boolean f1,f2;

		test_ball.y +=game.tile_size_px/2;
		test_ball.x +=game.tile_size_px/2;
		f1 = (rect_circle(rect,ball_to_rect(test_ball)));
		test_ball.x -=game.tile_size_px;
		f2 = (rect_circle(rect,ball_to_rect(test_ball)));


		if (f1 && f2) return 1;

		test_ball.y -=game.tile_size_px/2;
		f1 = (rect_circle(rect,ball_to_rect(test_ball)));
		test_ball.x +=game.tile_size_px;
		f2 = (rect_circle(rect,ball_to_rect(test_ball)));

		if (f1 && f2) return 1;

		return 0;
	}

	private int
	detect_ball_orientation(Ball ball, Rect rect, int wall_orient)
	{
    /*
       1|2
       3|4
       1 2
       ---
       3 4
     */
		Ball test_ball = new Ball(ball);


		if (wall_orient>0) {
			test_ball.y+=game.tile_size_px/2;

			if (rect_circle(rect,ball_to_rect(test_ball)))
				if (test_ball.vx<0)
					return 4;
				else
					return 3;
			else
			if (test_ball.vx<0)
				return 2;
			else
				return 1;
		}

		test_ball.x+=game.tile_size_px/2;
		if (rect_circle(rect,ball_to_rect(test_ball)))
			if (test_ball.vy>0)
				return 1;
			else
				return 3;
		else
		if (test_ball.vy>0)
			return 2;
		else
			return 4;
	}

	private void
	reflect_ball(Ball ball, Rect rect)
	{
		if ((last_render - ball.last_col) < 90) return;
		float dist = (float) Math.hypot(ball.x-ball.last_col_x, ball.y-ball.last_col_y);
		float epsilon = 5.0f;
		if (dist < epsilon)  return;

		float yaw = 1.0f + (float) (Math.random() * ((10.0f - 1.0f) + 1.0f)) -5.0f;
		float angle = 0.0f;
		int sign=1;

		int wall_orient =detect_wall_orientation(ball ,rect);
		int ball_orient = detect_ball_orientation(ball,rect,wall_orient);
		if (wall_orient == 0){
			angle = Math.abs(todeg(get_angle(ball.vx,ball.vy,1.0f,0.0f)));
			if (angle > 90) angle=180 -angle;

			if ((ball_orient) == 1) {
				sign=1;
			}
			if ((ball_orient) == 2){
				sign=-1;
			}
			if ((ball_orient) == 3){
				sign=-1;
			}
			if ((ball_orient) == 4) {
				sign=1;
			}
		}

		if (wall_orient == 1) {
			angle = Math.abs(todeg(get_angle(ball.vx,ball.vy,0.0f,1.0f)));
			if (angle > 90) angle=180 -angle;

			if ((ball_orient) == 1) {
				sign=1;
			}
			if ((ball_orient) == 2){
				sign=-1;
			}
			if ((ball_orient) == 3){
				sign=-1;
			}
			if ((ball_orient) == 4) {
				sign=1;
			}
		}
		angle *= 2*sign;
		if (angle < 8.0f || angle > -8.0f)
		angle = angle + 0.01f*yaw*angle;

		PointF v = new PointF();
		PointF src = new PointF();
		src.x = ball.vx; src.y = ball.vy;

		set_angle(v,src,torad(angle));
		ball.vx=v.x;
		ball.vy=v.y;
		ball.dx=0.0f;
		ball.dy=0.0f;

		ball.last_col= TimeUtils.millis();
		ball.last_col_x = ball.x;
		ball.last_col_y = ball.y;
		return;
	}

	private Rect snake_to_rect(int x, int y) {
		Rect r = new Rect();
		r.x = x*game.tile_size_px + game.tile_size_px / 2;
		r.y = y*game.tile_size_px + game.tile_size_px / 2;
		r.h = game.tile_size_px / 2;
		r.w = game.tile_size_px / 2;
		return r;
	}
    private Rect block_to_rect(int x, int y) {
        Rect r = new Rect();
        int tw = (TetrasnakonoidGame.max_width_tiles - TetrasnakonoidGame.tetris_w_tiles)/2;
        r.x = (x+ tw)*game.tile_size_px + game.tile_size_px / 2 ;
        r.y = y*game.tile_size_px + game.tile_size_px / 2;
        r.h = game.tile_size_px / 2;
        r.w = game.tile_size_px / 2;
        return r;
    }

	private boolean has_block(int i,int j)
	{
		if ((game.blocks[i][j] == 1) ||
			(game.blocks[i][j] == 2))
		return true;

		return false;
	}
	private void jingle_jingle(){
		long delta = TimeUtils.millis() - last_jingle;

		if (delta > jingle_timeout) {
			if (!sndmuted) { jingle.play(1.0f); last_jingle = TimeUtils.millis(); }
		}
	}
	private void coll_resolve() {
		if (rect_circle(game.wall[0], ball_to_rect(game.ball))) { reflect_ball(game.ball, game.wall[0]); }
		if (rect_circle(game.wall[1], ball_to_rect(game.ball) )) { reflect_ball(game.ball, game.wall[1]);}
		if (rect_circle(game.wall[2], ball_to_rect(game.ball))) { game_over();}
		if (rect_circle(game.wall[3], ball_to_rect(game.ball) )) { next_ball(); game.scores+=TetrasnakonoidGame.super_scores;  jingle_jingle();}

		if (rect_circle(game.ai.bb,ball_to_rect(game.ball))) {reflect_ball(game.ball, game.ai.bb); game.pc_turn = true;}
		if (rect_circle(game.pc.bb,ball_to_rect(game.ball))) {reflect_ball(game.ball, game.pc.bb); if (game.pc_turn) game.scores++; game.pc_turn = false; jingle_jingle();}

		if (rect_rect(game.ai.bb, snake_to_rect(game.headx, game.heady))) { game_over();}
		if (rect_rect(game.pc.bb, snake_to_rect(game.headx, game.heady))) { game_over();}


		for (int i =1; i<game.s_length; ++i) {
			PointT p = index_to_snake_xy(i);
			Rect r = snake_to_rect(p.x,p.y);
			if (rect_rect(game.ai.bb, r) ||
				rect_rect(game.pc.bb,r) ||
				rect_circle(r,ball_to_rect(game.ball))
				){
				if (i<TetrasnakonoidGame.snake_init_length_tiles) {
					for (int j=TetrasnakonoidGame.snake_init_length_tiles;j<game.s_length;++j) game.snake_directions.removeLast();
					game.s_length = TetrasnakonoidGame.snake_init_length_tiles;
				}
				else {
					for (int j=i;j<game.s_length;++j) game.snake_directions.removeLast();
					game.s_length = i; break;

				}
			}
		}

		int width = get_tetramino_width(game.tetramino_type, game.tetramino_rot);
		int height = get_tetramino_height(game.tetramino_type, game.tetramino_rot);
		for (int i =game.tetramino_y; i< game.tetramino_y+ height;++i) {
			for (int j=game.tetramino_x; j<game.tetramino_x + width;++j) {
				if (has_block(i,j)) {
					if (rect_circle(block_to_rect(j, i), ball_to_rect(game.ball))) {
						clear_tetramino();
						next_tetramino();
					}
				}
			}
		}

		for (int i =0; i< TetrasnakonoidGame.tetris_h_tiles;++i) {
			for (int j = 0; j< TetrasnakonoidGame.tetris_w_tiles;++j) {
				if (has_block(i,j)) {
					if (rect_circle(block_to_rect(j, i), ball_to_rect(game.ball))) {
						reflect_ball(game.ball, block_to_rect(j,i));
					}
				}
			}
		}

	}

	private void animate_rack(){
		int sign = 0;
		int epsilon = 5;
		if (game.pc.bb.y - game.pc.target_y > 0) sign = -1; else sign = 1;

		if (Math.abs(game.pc.bb.y - game.pc.target_y) > epsilon)
		game.pc.bb.y += Gdx.graphics.getDeltaTime() * game.pc.v * sign;

		if (game.ai.bb.y - game.ai.target_y > 0) sign = -1; else sign = 1;

		if (Math.abs(game.ai.bb.y - game.ai.target_y) > epsilon)
		game.ai.bb.y += Gdx.graphics.getDeltaTime() * game.ai.v * sign;
	}

	private void check_snake_collisions() {
		int width = get_tetramino_width(game.tetramino_type, game.tetramino_rot);
		int height = get_tetramino_height(game.tetramino_type, game.tetramino_rot);
		for (int i =game.tetramino_y; i< game.tetramino_y+ height;++i) {
			for (int j=game.tetramino_x; j<game.tetramino_x + width;++j) {
				if (has_block(i,j)) {
					if (rect_rect(snake_to_rect(game.headx, game.heady), block_to_rect(j, i))) {
						clear_tetramino();
						next_tetramino();
						game.scores += TetrasnakonoidGame.super_scores; jingle_jingle();
					}
				}
			}
		}
		for (int i =game.tetramino_y; i< game.tetramino_y+ height;++i) {
			for (int j=game.tetramino_x; j<game.tetramino_x + width;++j) {
				for (int k = 1; k<game.s_length;k++) {
					PointT p = index_to_snake_xy(k);
					Rect r = snake_to_rect(p.x,p.y);
					if ((has_block(i,j)) && (rect_rect(block_to_rect(j,i), r))) {
						if (k < TetrasnakonoidGame.snake_init_length_tiles) {
							for (int l=TetrasnakonoidGame.snake_init_length_tiles;l<game.s_length;++l) game.snake_directions.removeLast();
							game.s_length = TetrasnakonoidGame.snake_init_length_tiles;
						} else {
							for (int l=k;l<game.s_length;++l) game.snake_directions.removeLast();
							game.s_length = k;
							break;
						}
					}
				}
			}
		}

		for (int i =0; i< TetrasnakonoidGame.tetris_h_tiles;++i) {
			for (int j=0; j<TetrasnakonoidGame.tetris_w_tiles;++j) {
				if (game.blocks[i][j] == 1) {
					if (rect_rect(snake_to_rect(game.headx, game.heady), block_to_rect(j, i))) {
						game_over();
					}
				}
			}
		}

		for (int i=1; i<game.s_length; ++i) {
			PointT p =  index_to_snake_xy(i);
			if ((p.x == game.headx) &&
					(p.y == game.heady)){
				game_over();
			}
		}

		if (rect_rect(snake_to_rect(game.foodx, game.foody), ball_to_rect(game.ball))){
		    next_lulz_ball();
        }
	}
	private void recalc_game_difficulty()
	{
		int k = game.scores / 50;
		game.difficulty_a=k;
		game.difficulty_s=k;
		game.difficulty_t=k;
	}
	private void animate_snake(){
		game.snake_ani_acc+=Gdx.graphics.getDeltaTime();
		int speed = TetrasnakonoidGame.snake_speed_tiles_per_sec + game.difficulty_s;
		if (game.snake_ani_acc > 1/(float)speed) {
			recalc_game_difficulty();
			check_snake_collisions();
			if (game.next_snake_dir > 0) {
				game.snake_directions.set(0,game.next_snake_dir);
				game.next_snake_dir = 0;
			}

		    int e = game.snake_directions.get(0);
			switch (e)
			{
				case 1:
					 if (game.heady <= 0) game.heady = TetrasnakonoidGame.max_height_tiles; else game.heady--; break;
				case 2:
					 if (game.heady >= TetrasnakonoidGame.max_height_tiles) game.heady = 0; else 	game.heady++; break;
				case 3:
					 if (game.headx <= 0) game.headx = TetrasnakonoidGame.max_width_tiles; else game.headx--;
					break;
				case 4:
					 if (game.headx >= TetrasnakonoidGame.max_width_tiles) game.headx = 0; else  game.headx++;
					break;
				default:
					break;

			}
			game.snake_directions.addFirst(e);
			if ((game.headx == game.foodx) && (game.heady == game.foody)) {
				next_food(); game.s_length++; game.scores++; jingle_jingle();
			}
			else {
				game.snake_directions.removeLast();
			}
			game.snake_ani_acc = 0.0f;
			game.s_delta =0.0f;
		}
	}

	private boolean same_signs(int a, int b) {
		return (a >= 0) ^ (b < 0);
	}

	private PointT lines_intersect( int x1, int y1,
						 int x2, int y2,

						 int x3, int y3,
						 int x4, int y4)
	{
		PointT out = new PointT(0,0);
		int a1, a2, b1, b2, c1, c2; /* Coefficients of line eqns. */
		int r1, r2, r3, r4;         /* 'Sign' values */
		int denom, offset, num;     /* Intermediate values */

		/* Compute a1, b1, c1, where line joining points 1 and 2
		 * is "a1 x  +  b1 y  +  c1  =  0".
		 */

		a1 = y2 - y1;
		b1 = x1 - x2;
		c1 = x2 * y1 - x1 * y2;

		/* Compute r3 and r4.
		 */


		r3 = a1 * x3 + b1 * y3 + c1;
		r4 = a1 * x4 + b1 * y4 + c1;

		/* Check signs of r3 and r4.  If both point 3 and point 4 lie on
		 * same side of line 1, the line segments do not intersect.
		 */

		if ( r3 != 0 &&
				r4 != 0 &&
				same_signs( r3, r4 )) {
			out.flag = 0; return out;
		}

		/* Compute a2, b2, c2 */

		a2 = y4 - y3;
		b2 = x3 - x4;
		c2 = x4 * y3 - x3 * y4;

		/* Compute r1 and r2 */

		r1 = a2 * x1 + b2 * y1 + c2;
		r2 = a2 * x2 + b2 * y2 + c2;

		/* Check signs of r1 and r2.  If both point 1 and point 2 lie
		 * on same side of second line segment, the line segments do
		 * not intersect.
		 */

		if ( r1 != 0 &&
				r2 != 0 &&
				same_signs( r1, r2 )) {
				out.flag = 0;
				return out;
		}

		/* Line segments intersect: compute intersection point.
		 */

		denom = a1 * b2 - a2 * b1;
		if ( denom == 0 ) {
			out.flag = 1;
			return out;
		}
		offset = denom < 0 ? - denom / 2 : denom / 2;

		/* The denom/2 is to get rounding instead of truncating.  It
		 * is added or subtracted to the numerator, depending upon the
		 * sign of the numerator.
		 */

		num = b1 * c2 - b2 * c1;
    out.x = ( num < 0 ? num - offset : num + offset ) / denom;

		num = a2 * c1 - a1 * c2;
    out.y = ( num < 0 ? num - offset : num + offset ) / denom;

		out.flag = 2;
		return out;
	}

	private void rack_ai() {
		boolean roll = false;
		game.rack_ai_acc+=Gdx.graphics.getDeltaTime();
		if (game.rack_ai_acc > 0.3f) {
			Random r = new Random();
			if (r.nextInt() % 3 == 1) { roll = true; }
			int max = game.vp_h - game.ai.bb.h;
			int min = game.ai.bb.h;
			if (game.ball.vx < 0) {
				if (roll)
					game.ai.target_y = min + (int) (Math.random() * ((max - min) + 1));

			} else {
				if (game.ball.x > game.vp_w/3) {
					int x2 = game.ball.x+ (int) (2*game.ball.vx);
					int y2 = game.ball.y+ (int) (2*game.ball.vy);
					PointT p = lines_intersect(game.ball.x, game.ball.y, x2, y2, game.vp_w,20, game.vp_w, game.vp_h);

					if (p.flag == 2) {
						game.ai.target_y = game.vp_h/2;
						if ((p.y < max) && (p.y > min)) game.ai.target_y = p.y;

						if ((p.y > max) && (p.y - max > game.vp_h/2)) game.ai.target_y = min;
						if ((p.y < min) && (Math.abs(p.y - min) > game.vp_h/2)) game.ai.target_y = max;
					}
				}
				else {
					if (roll)
						game.ai.target_y = min + (int) (Math.random() * ((max - min) + 1));
				}
			}
			game.rack_ai_acc = 0.0f;
		}
	}
	private PointF snake_to_render(int x, int y)
	{
		PointF out = new PointF();
		out.x = x*game.tile_size_px + game.vp_x;
		out.y = y*game.tile_size_px + game.vp_y;
		return out;
	}

	private PointT index_to_snake_xy(int id)
	{
		PointT out = new PointT(game.headx,game.heady);

		for (int i =0;i<id; ++i) {
			switch (game.snake_directions.get(i+1)){
				case 1:
					if (out.y >= TetrasnakonoidGame.max_height_tiles) out.y = 0; else out.y++ ; break;
				case 2:
					if (out.y <= 0) out.y = TetrasnakonoidGame.max_height_tiles; else out.y--; break;
				case 3:
					if (out.x >= TetrasnakonoidGame.max_width_tiles) out.x = 0; else out.x++;
					break;
				case 4:
					if (out.x <= 0 ) out.x =TetrasnakonoidGame.max_width_tiles; else out.x--;
					break;
				default:
					break;
			}
		}

		return out;
	}

	private float dir_to_rot(int dir){
		switch (dir){
			case 1:
				return 180.0f;
			case 2:
				return 0.0f;
			case 3:
				return 90.0f;
			case 4:
				return -90.0f;
			default:
				return 0.0f;
		}
	}
	private float fix_anglebody( int d1, int d2)
	{
		if ((d1 == 1) && (d2 ==4)) return 90f;
		if ((d1 == 2) && (d2 ==3)) return 90f;
		if ((d1 == 3) && (d2 ==1)) return 90f;
		if ((d1 == 4) && (d2 ==2)) return 90f;

		return 0;
	}

	private boolean check_tetramino_move(boolean left){
		if (left) game.tetramino_x --; else game.tetramino_x++;

		put_tetramino(game.tetramino_type, game.tetramino_rot, game.tetramino_x, game.tetramino_y);
		boolean flag = check_tetramino();
		unput_tetramino();
		clear_tetramino();

		if (left) game.tetramino_x++; else game.tetramino_x--;

		put_tetramino(game.tetramino_type, game.tetramino_rot, game.tetramino_x, game.tetramino_y);

		return !flag;
	}

	private boolean check_tetramino_rot(){
		next_tetr_rot();

		put_tetramino(game.tetramino_type, game.tetramino_rot, game.tetramino_x, game.tetramino_y);
		boolean flag = check_tetramino();
		unput_tetramino();
		clear_tetramino();

		prev_tetr_rot();

		put_tetramino(game.tetramino_type, game.tetramino_rot, game.tetramino_x, game.tetramino_y);

		return !flag;
	}


	private boolean check_tetramino(){
		for (int i = 0; i < TetrasnakonoidGame.tetris_h_tiles; ++i ) {
			for (int j = 0; j < TetrasnakonoidGame.tetris_w_tiles; ++j ){
				if (game.blocks[i][j] == 3) {
					return true;
				}
			}
		}
		return false;
		
	}
	private boolean check_line(int id)
	{
		boolean flag = true;
		for (int i =0; i<TetrasnakonoidGame.tetris_w_tiles;++i) {
			if (game.blocks[id][i] == 0) { flag = false; return flag; }
		}
		return flag;
	}
	private boolean clear_lines()
	{
		boolean flag = true;
		int lineid = 0;
		for (int i=0;i< TetrasnakonoidGame.tetris_h_tiles;++i) {
			flag = check_line(i);
			if (flag) {lineid = i; break;}
		}
		if (flag) {
			for (int i =0; i<TetrasnakonoidGame.tetris_w_tiles;++i) {
				game.blocks[lineid][i] = 0;
			}
			for (int i =lineid; i<TetrasnakonoidGame.tetris_h_tiles-1;++i) {
				for (int j = 0; j < TetrasnakonoidGame.tetris_w_tiles; ++j) {
						game.blocks[i][j] = game.blocks[i+1][j];
				}
			}
			game.scores+=TetrasnakonoidGame.super_scores; jingle_jingle();
		}
		return flag;
	}
	private void animate_tetris(){
		game.tetris_ani_acc+=Gdx.graphics.getDeltaTime();
		int speed = TetrasnakonoidGame.tetris_speed_tiles_per_sec + game.difficulty_t;
		if (game.tetris_ani_acc > 1/(float) speed) {

			game.tetramino_y--;
            clear_tetramino();

            put_tetramino(game.tetramino_type, game.tetramino_rot, game.tetramino_x, game.tetramino_y);

			if (check_tetramino()) {
				unput_tetramino();
				game.tetramino_y++;
				put_tetramino(game.tetramino_type, game.tetramino_rot, game.tetramino_x, game.tetramino_y);
				tetramino_to_block();
                while(clear_lines()){;}
				next_tetramino();
                game.scores++; jingle_jingle();
			}
			if (game.tetramino_y == 0) {tetramino_to_block(); while(clear_lines()){;} next_tetramino(); game.scores++; jingle_jingle();}


			for (int i =0;i<TetrasnakonoidGame.tetris_w_tiles; ++i) {
				if (game.blocks[TetrasnakonoidGame.tetris_h_tiles][i] == 1) game_over();
			}

			game.tetris_ani_acc = 0.0f;
		}
	}

	private PointF block_index_to_sprite(int i, int j)
	{
		PointF p = new PointF();
		p.y = i*game.tile_size_px + game.vp_y;
		p.x = j*game.tile_size_px + game.vp_x+15*game.tile_size_px;
		return p;
	}

	private  void render_game() {

		if (DEBUG) {
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.rect(game.vp_x, game.vp_y,  game.vp_w, game.vp_h);
			shapeRenderer.end();
		}
		rack_ai();
		coll_resolve();
		animate_ball();
		if (game.has_lulz_ball) animate_lulz_ball();
		animate_rack();
		animate_snake();
		animate_tetris();
		update_sprites();

		batch.begin();
		spr_pc_racket.draw(batch);
		spr_ai_racket.draw(batch);
	    spr_ball.draw(batch);
        if (game.has_lulz_ball) spr_lulz_ball.draw(batch);
	    PointF p;
		PointT p2;
			for (int i = 0; i < game.s_length; ++i) {
				if (i == 0) {

					p = snake_to_render(game.headx, game.heady);
					spr_head.setPosition(p.x, p.y);
					spr_head.setRotation(dir_to_rot(game.snake_directions.get(0)));
					spr_head.draw(batch);
					continue;
				}

				p2 = index_to_snake_xy(i);
				p = snake_to_render(p2.x, p2.y);
				if (i == game.s_length - 1) {
					spr_tail.setPosition(p.x, p.y);
					spr_tail.setRotation(dir_to_rot(game.snake_directions.get(i)));
					spr_tail.draw(batch);
				} else {
					int d1 = game.snake_directions.get(i);
					int d2 = game.snake_directions.get(i + 1);
					float rot = dir_to_rot(d1);
					if (d1 != d2) {
						rot += fix_anglebody(d2, d1);
						spr_anglebody.setRotation(rot);
						spr_anglebody.setPosition(p.x, p.y);
						spr_anglebody.draw(batch);
					} else {
						spr_body.setRotation(rot);
						spr_body.setPosition(p.x, p.y);
						spr_body.draw(batch);

					}
				}

			}
			p = snake_to_render(game.foodx, game.foody);
			spr_food.setPosition(p.x, p.y);
			spr_food.draw(batch);

		for (int i = 0; i < TetrasnakonoidGame.tetris_h_tiles; ++i)
			for (int j = 0; j < TetrasnakonoidGame.tetris_w_tiles; ++j){
				if ((game.blocks[i][j] == 1) || (game.blocks[i][j] == 2)) {
					p = block_index_to_sprite(i,j);
					spr_tetrotile.setPosition(p.x, p.y);
					spr_tetrotile.draw(batch);
				}
		}
		spr_tetroborderl.draw(batch);
		spr_tetroborderr.draw(batch);
	    batch.end();


		ingame.draw();

		last_render = TimeUtils.millis();

	}
	private  void render_help() {
		batch.begin();
		spr_help_background.draw(batch);
		spr_help_overlay.draw(batch);
		batch.end();
		help.draw();
	}

	private  void render_gameover() {
		batch.begin();
		batch.draw(game_over_background,0,0);
		batch.end();
		gameover.draw();
	}


	private  void render_credits() {
		credits.draw();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void render () {
		if (!game_over_flag) {
			Gdx.gl.glClearColor(OLD_TETRIS_COLOR[0], OLD_TETRIS_COLOR[1], OLD_TETRIS_COLOR[2], 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			switch (game_state) {
				case 0: render_gui();
					break;
				case 1:  render_game();
					break;
				case 2:  render_help();
					break;
				case 3: render_credits();
					break;
				case 4: render_gameover();
					break;
				default:
					render_game();
					break;
			}
		}
		if (game_over_flag) {
			game_over_background = ScreenUtils.getFrameBufferTexture();
			game_over_flag = false;
		}

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		old_tetris_melody.dispose();
		jingle.dispose();
		high_score_jingle.dispose();
		fail_jingle.dispose();
		ui.dispose();
		credits.dispose();
		help.dispose();
		gameover.dispose();
	}
}
