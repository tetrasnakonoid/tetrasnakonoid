/*
 *  Это что за покемон? Тетраснэйконоид.
 *  Или то самое чувство когда стал настолько тонким что
 *  сам уже не можешь отличить собственный троллинг от реальности.
 *  Даже не спрашивайте что я курил, чтобы наговнокодить это.
 *  Энтерпрайз на Яве. Покрытие юнит-тестами.
 *  Кукарек<кококо> кукарек = new Кукарек<кококо>;
 *  85 гигабайт памяти должно хватить АХХАХААХАХ
 *  В конце концов я простой охуевающий байтослесарь.
 */
/* C is better for me than Java. Sorry for this mess. */
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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.LinkedList;
import java.util.Random;

public class Tetrasnakonoid extends ApplicationAdapter implements ApplicationListener {
	public static final boolean DEBUG = false;
	public static final String LOG_TAG = "TTS_DEBUG";

	public static final String self_name = "Tetransnakonoid 9000 PRO";
	public static final int Answer = 42;
	public static final String version = "v9000.1";
	public static final String corporation = "One Man Company That Makes Everything";
	public static final String mailto = "fuck@tetrasnakonoid.ru";
	
	public static final int favourite_width = 1920;	
	public static final int favourite_height = 1080;

	private ShapeRenderer shapeRenderer;

	private Stage ui, ingame, ingameeasy, help, gameover, credits;
	private ScreenViewport viewport;
	private SpriteBatch batch;

	private Texture helpup, helpdown, mutesnd, unmutesnd, mutemusic, unmutemusic, loginup, logindown, ratemeup, ratemedown, donatebtc, donateltc, donateeth, donateusd, halloffame, hardcoreup, hardcoredown, newgame;
	private ImageButton btn_help, btn_mutesnd, btn_mutemusic, btn_login, btn_donate_btc, btn_donate_ltc, btn_donate_eth, btn_donate_usd, btn_rateme, btn_halloffame, btn_hardcore, btn_new_game;
	private Drawable drw_helpup, drw_helpdown, drw_mutesnd, drw_unmutesnd, drw_mutemusic, drw_unmutemusic, drw_loginup, drw_logindown,
			drw_ratemeup, drw_ratemedown, drw_donate_btc, drw_donate_ltc, drw_donate_eth, drw_donate_usd, drw_hall_of_fame, drw_hardcoreup, drw_hardcoredown, drw_new_game;
	private Label lbl_high_scores_out, lbl_last_try_out, lbl_version;

	private ParticleEffect hardcorefire;

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
	private Sprite spr_pc_racket, spr_ai_racket, spr_ball, spr_lulz_ball, spr_false_ball;
	private TextureRegion ball_frames[];
	private int total_ball_frames = 0;
	private int current_ball_frame = 0;
	private float ball_animation_acc = 0;
	private float br_angle = 0;

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

	private Texture gamepad_up, gamepad_down, gamepad_right, gamepad_left, gamepad_rot;
	private Drawable drw_gamepadup, drw_gamepaddown, drw_gamepadleft, drw_gamepadright,  drw_gamepadrot;
	private ImageButton btn_gamepadup, btn_gamepaddown, btn_gamepadleft, btn_gamepadright, btn_gamepadrot;

	private Texture help_background, help_overlay, easy_help_overlay, help_quit;
	private Sprite spr_help_background, spr_help_overlay, spr_easy_help_overlay;
	private Drawable drw_help_quit;
	private ImageButton btn_help_quit;
	private float help_fadeout = 1.0f;

	private TetrasnakonoidUsernameDialog ask_username;

	private Texture sharefb, sharetw, sharevk, sharegh, game_over_quit;
	private TextureRegion game_over_background;
	private Drawable drw_game_over_back, drw_sharefb, drw_sharetw, drw_sharevk, drw_sharegh;
	private ImageButton btn_game_over_back, btn_sharefb, btn_sharetw, btn_sharevk, btn_sharegh;
	private Sprite hardcoremark;
	private Image logo;

	private BitmapFont fnt_game_over;
	private Label lbl_scores_final, lbl_quote_final;
	private Table game_over_layout;

	private ParticleEffect explosion;

	private Table credits_layout;
	private Label lbl_credits;
	private Texture credits_quit;
	private Drawable drw_credits_quit;
	private ImageButton btn_credits_quit;

	private TetrasnakonoidGame game;

	private static final float[] OLD_TETRIS_COLOR = {0.7529f, 0.8078f, 0.6352f};
	private static final String motto = "protip: you can't";
	private static final String motto2 = "There is no Snakotetrisoid is a lie.";

	private static final String selfURI = "https://tetrasnakonoid.ru";

	private static final String ratemeURIAndroid = "https://play.google.com/store/apps/details?id=com.tetrasnakonoid.game";
	private static final String ratemeURIApple = "";
	private static final String ratemeURIOther = selfURI;

	private static final String githubURI = "https://github.com/tetrasnakonoid/tetrasnakonoid";
	private static final String facebookURI = "https://facebook.com";
	private static final String twitterURI = "https://twitter.com";
	private static final String vkURI = "https://vk.com";

	private static final String donateURI = "https://tetrasnakonoid.ru/donate";
	private static final String donateBTCURI = "https://tetrasnakonoid.ru/donate/btc";
	private static final String donateLTCURI = "https://tetrasnakonoid.ru/donate/ltc";
	private static final String donateETHURI = "https://tetrasnakonoid.ru/donate/eth";
	private static final String donateUSDURI = "https://tetrasnakonoid.ru/donate/usd";
	private static final String halloffameURI = "https://tetrasnakonoid.ru/backers/new";

	private static final String ETHWallet = "ETH: 0x7d3935e9b579a53B23d1BC14C23fdDafE9f3d522";
	private static final String BTCWallet = "BTC: 18gm58hZ7avF18tvhoQmEEkKFTeh2WANoG";
	private static final String LTCWallet = "LTC: 3HR93M2JPvqBVQrbzETAArUZaq9hpDJfZQ";

	private int game_state = 0;

	private boolean sndmuted = false;
	private boolean musicmuted = true;
	private String player_name = "Anonymous";

	private boolean has_high_scores = false;
	private int high_score = 0;
	private String top_player_name = "Anonymous";
	private int high_score_easy = 0;
	private String top_player_name_easy = "Anonymous";

	private int last_score = 0;
	private String last_player_name = "Anonymous";
	private int last_score_easy = 0;
	private String last_player_name_easy = "Anonymous";

	private long last_render = 0;
	private boolean game_over_flag = false;
	private boolean first_try = true;
	private long last_game_over_time = 0;
	private long last_new_game_time = 0;
	private boolean only_once = true;

	public long desktop_state_timeout = 0;

	private boolean do_resize_next_frame = false;

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
			while (( Math.abs((OLD_TETRIS_COLOR[0] - r)) < 0.1f) &&
					(Math.abs((OLD_TETRIS_COLOR[1] - g)) < 0.1f) &&
					(Math.abs((OLD_TETRIS_COLOR[2] - b)) < 0.1f));

		return new Color(r, g, b, 1);
	}

	private void detect_tile_size(int h, int w) {
		if ((h==0) || (w==0)) return;
		int a = h / TetrasnakonoidGame.max_height_tiles;
		int b = w / TetrasnakonoidGame.max_width_tiles;

		game.tile_size_px = Math.min(a, b);
		if (game.tile_size_px % 2 == 1) game.tile_size_px -= 1;

		game.k = (float) game.tile_size_px / (float) TetrasnakonoidGame.sprite_tile_size_px;

		game.vp_h = game.tile_size_px * TetrasnakonoidGame.max_height_tiles;
		game.vp_w = game.tile_size_px * TetrasnakonoidGame.max_width_tiles;

		game.vp_x = (w - game.vp_w) / 2;
		game.vp_y = (h - game.vp_h) / 2;

		if (DEBUG) {Gdx.app.debug(LOG_TAG, "VP X: " + String.valueOf(game.vp_x) + "VP Y: " + String.valueOf(game.vp_y) + "VP H: " + String.valueOf(game.vp_h) + "VP W: " + String.valueOf(game.vp_w)); }
	}

	private void set_random_colors() {
		game.a_color = getRandomColor();
		game.s_color = getRandomColor();
		game.t_color = getRandomColor();
	}

	private void init_pc_racket() {
		spr_pc_racket.setColor(game.a_color);


		game.pc.v = TetrasnakonoidGame.racket_speed_tiles_sec*game.tile_size_px;

		spr_pc_racket.getHeight();
		game.pc.bb.h = TetrasnakonoidGame.racket_length_tiles * game.tile_size_px / 2;
		game.pc.bb.w = game.tile_size_px / 2;

		game.pc.bb.x = 3*game.tile_size_px/2;

		int max = game.vp_h - game.pc.bb.h;
		int min = game.pc.bb.h;
		game.pc.bb.y = min + (int) (Math.random() * ((max - min) + 1));
		game.pc.target_y = game.pc.bb.y;

	}

	private void init_ai_racket() {
		spr_ai_racket.setColor(game.a_color);

		game.ai.v = TetrasnakonoidGame.racket_speed_tiles_sec*game.tile_size_px;

		game.ai.bb.h = TetrasnakonoidGame.racket_length_tiles * game.tile_size_px / 2;
		game.ai.bb.w = game.tile_size_px / 2;

		game.ai.bb.x = game.vp_w - 3*game.tile_size_px/2;
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

	private void next_pitch() {
		game.kicked_back = false;
		game.got_food = false;
		game.tetramino_landed = false;
		game.pitch_out = false;
		game.easy_mode_state = 0;
		Random rand = new Random();
		game.ball.v = 0.25f * game.vp_w + 0.1f*game.difficulty_a;
		game.ball.v *=0.4;

		float angle = 30.0f + (float) (Math.random() * ((150.0f - 30.0f) + 1.0f)) - 90.0f;
		game.pc_pitcher = rand.nextBoolean();
		if (!game.pc_pitcher) {
			game.ball.x = game.ai.bb.x - game.tile_size_px;
			game.ball.y = game.ai.bb.y;
			angle -= 180.0f;

			game.false_ball.x = game.pc.bb.x + game.tile_size_px;
			game.false_ball.y = game.pc.bb.y;

		} else {
			game.ball.x = game.pc.bb.x + game.tile_size_px;
			game.ball.y = game.pc.bb.y;

			game.false_ball.x = game.ai.bb.x - game.tile_size_px;
			game.false_ball.y = game.ai.bb.y;

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
		next_easy_snake();
	}

	private void init_ball() {
		if (game.hardcore)
			next_ball();
		else
			next_pitch();

		spr_ball.setColor(game.a_color);
        spr_lulz_ball.setColor(game.a_color);
		spr_false_ball.setColor(game.a_color);

	}

	private void init_arcanoid() {
		game.difficulty_a = 0;
		init_pc_racket();
		init_ai_racket();
		init_ball();

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
		game.wall[0].y = 2*y + game.tile_size_px/2;
		game.wall[0].h = game.tile_size_px/2;
		game.wall[0].w = w;

		game.wall[1] = new Rect();
		game.wall[1].x = x;
		game.wall[1].y = 0 - game.tile_size_px/2;
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
		int width =(TetrasnakonoidGame.max_width_tiles - game.tetris_w_tiles)/2 - 2;
		int x = r.nextInt(width);
		if (r.nextInt(2) == 1) game.foodx = 2 + x; else game.foodx = 2 + width + game.tetris_w_tiles + x;
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

	private void change_ball_course()
	{
		float angle = 30.0f + (float) (Math.random() * ((150.0f - 30.0f) + 1.0f)) - 90.0f;

		if (!game.pc_pitcher) {
			angle -= 180.0f;
		}
		angle = torad(angle);
		game.ball.v *=1 + (Math.random() + Math.random() + Math.random())/3;
		game.ball.vx = (float) Math.cos(angle) * game.ball.v;
		game.ball.vy = (float) Math.sin(angle) * game.ball.v;
		game.ball.dx = 0.0f;
		game.ball.dy = 0.0f;
		game.ball.angle = angle;
	}

	private boolean check_food_easy()
	{
		PointT p1 = new PointT(game.foodx,game.foody);
		PointT p2 = snake_to_tetris(p1);

		if ((p2.x<0) || (p2.y<0) || (p2.x>=game.tetris_w_tiles) || (p2.y>=game.tetris_h_tiles)) { return true;}

		if (game.blocks[p2.y][p2.x] == 1) { return true; }

		return false;
	}
	private void next_food_easy()
	{
		Random r = new Random();
		int x = (TetrasnakonoidGame.max_width_tiles - game.tetris_w_tiles)/2;
		int h = get_tetramino_height(game.tetramino_type, game.tetramino_rot);

		int cnt =0;
		do {
			boolean left = r.nextBoolean();
			int deltax = r.nextInt(4) + 1;
			int deltay = r.nextInt(5) + 3;
			if (game.tetris_reverse_gravity) {
				if (left) {
					game.foodx = x + game.tetramino_x - deltax;
				}
				else{
					game.foodx = x + game.tetramino_x + deltax;
				}
				game.foody = game.tetramino_y + h + deltay;
			} else {
				if (left) {
					game.foodx = x + game.tetramino_x - deltax;
				}
				else {
					game.foodx = x + game.tetramino_x + deltay;
				}
				game.foody = game.tetramino_y - deltay;
			}
			cnt++;
			if (cnt > 100) {game.got_food = true;}
		} while(check_food_easy());
	}

	private void next_food() {
		game.snake_hunger = 1.2f*game.vp_w;
		do {next_food_xy();} while(food_in_snake());

		Random r = new Random();
		game.food_id = r.nextInt(TetrasnakonoidGame.total_foods);

		spr_food.setRegion(TetrasnakonoidGame.sprite_tile_size_px*(game.food_id),0 , TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px);
	}

	private boolean check_snake_respawn(int dir)
	{
		PointT in = new PointT(game.headx, game.heady);
		PointT p = snake_to_tetris(in);

		if (has_block(p.y,p.x)) return true;

		int cnt = 0;
		int min =  p.x - TetrasnakonoidGame.snake_init_length_tiles_easy;
		int max = p.x + TetrasnakonoidGame.snake_init_length_tiles_easy;
		if ((dir == 3) || (dir == 4)) {
			if (min<0) min =0;
			if (max >= TetrasnakonoidGame.tetris_w_tiles_easy) max = TetrasnakonoidGame.tetris_w_tiles_easy-1;
			for (int i =min; i<=max;  ++i) {
				if (has_block(p.y,i)) return true;
			}
		}

		min =  p.y - TetrasnakonoidGame.snake_init_length_tiles_easy;
		max = p.y + TetrasnakonoidGame.snake_init_length_tiles_easy;
		if ((dir == 2) || (dir == 1)) {
			if (min<0) min =0;
			if (max >= TetrasnakonoidGame.tetris_h_tiles_easy) max = TetrasnakonoidGame.tetris_h_tiles_easy-1;
			for (int i = min;i<= max ; ++i) {
				if (has_block(i,p.x)) return true;
			}
		}
		return false;
	}
	private void next_easy_snake() {
		Random r = new Random();
		int x = (TetrasnakonoidGame.max_width_tiles - game.tetris_w_tiles)/2;
		int rd = 0;
		int cnt = 0;
		do {
			rd = r.nextInt((4 - 1) + 1) + 1;
			game.heady = r.nextInt(TetrasnakonoidGame.max_height_tiles);
			game.headx = r.nextInt(game.tetris_w_tiles) + x;
			cnt++; if (cnt>1000) game_over(20);
		} while (check_snake_respawn(rd));

		game.s_length = TetrasnakonoidGame.snake_init_length_tiles_easy;

		game.snake_directions.clear();
		for (int i=0;i<game.s_length;i++) {
			game.snake_directions.add(rd);
		}
	}

	private void init_snake(){
		game.snake_hunger = 1.2f*game.vp_w;
		game.difficulty_s = 0;
		game.snake_ani_acc = 0;
		game.s_delta = 0;
		game.s_length = TetrasnakonoidGame.snake_init_length_tiles;
		int index = 0;

		Random r = new Random(); int rd = r.nextInt((4 - 1) + 1) + 1;
		if (game.hardcore) {
			game.snake_directions.clear();
			for (int i=0;i<game.s_length;i++) {
				game.snake_directions.add(rd);
			}
			next_food_xy();
			game.headx = game.foodx;
			game.heady = r.nextInt(TetrasnakonoidGame.max_height_tiles);
			next_food();
		}
		else{
			next_easy_snake();
		}


		spr_food.setColor(game.s_color);
		spr_head.setColor(game.s_color);
		spr_tail.setColor(game.s_color);
		spr_body.setColor(game.s_color);
		spr_anglebody.setColor(game.s_color);


	}

	private void tetramino_to_block() {
		for (int i = 0;i < game.tetris_h_tiles + 4 ; ++i)
			for (int j = 0; j < game.tetris_w_tiles ; ++j) {
				if (game.blocks[i][j] == 2) game.blocks[i][j] = 1;
			}
	}

	private void clear_tetramino() {
			for (int i = 0; i < game.tetris_h_tiles + 4; ++i)
				for (int j = 0; j < game.tetris_w_tiles; ++j){
				if (game.blocks[i][j] == 2) game.blocks[i][j] = 0;
			}
	}

	private void unput_tetramino() {
		for (int i = 0; i < game.tetris_h_tiles + 4; ++i)
			for (int j = 0; j < game.tetris_w_tiles; ++j){
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
						game.blocks[y][++x] += 2;
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
		game.tetramino_y = game.tetris_h_tiles-1;
		game.tetramino_x = (game.tetris_w_tiles - get_tetramino_width(game.tetramino_type, game.tetramino_rot))/2;
		put_tetramino(game.tetramino_type, game.tetramino_rot, game.tetramino_x, game.tetramino_y);
	}
	   private void init_tetris(){
		game.difficulty_t = 0;
		game.tetris_ani_acc  = 0;

		for (int i =0; i < game.tetris_h_tiles+4; ++i) {
		   for (int j = 0; j < game.tetris_w_tiles + 4; ++j) {
			   game.blocks[i][j] = 0;
		   }
		}
		game.next_tetramino_dir = 0;
		if (game.hardcore) next_tetramino();

		spr_tetrotile.setColor(game.t_color);

		spr_tetroborderl.setColor(game.t_color);
		spr_tetroborderr.setColor(game.t_color);
		int lbx = (TetrasnakonoidGame.max_width_tiles - game.tetris_w_tiles)/2;
		spr_tetroborderl.setPosition(lbx* game.tile_size_px + game.vp_x, game.vp_y);
		spr_tetroborderr.setPosition((lbx + game.tetris_w_tiles)  * game.tile_size_px + game.vp_x, game.vp_y);
		btn_tetr_move.setPosition(lbx* game.tile_size_px + game.vp_x,0);

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
		boolean flag = false;
		if ((game.scores > high_score) && (game.hardcore)) {
			top_player_name = player_name;
			high_score = game.scores;

			out += "\n";
			out += String.valueOf(game.scores);
			out += "\n";
			out += top_player_name_easy;
			out += "\n";
			out += String.valueOf(high_score_easy);
			scores.writeString(out,false);
			flag = true;
		}

		if ((game.scores > high_score_easy) && (!game.hardcore)) {
			top_player_name_easy = player_name;
			high_score_easy = game.scores;

			out = top_player_name;
			out += "\n";
			out += String.valueOf(high_score);
			out += "\n";
			out += player_name;
			out += "\n";
			out += String.valueOf(game.scores);
			scores.writeString(out,false);
			flag = true;
		}
		if (flag) {
			lbl = "TOP: ";
			lbl += last_player_name;
			lbl += " ";
			lbl += String.valueOf(last_score);
			lbl_high_scores_out.setText(lbl);
		}
		String out2 = player_name;
		out2+="\n";
		if  (sndmuted) out2 +=String.valueOf(0); else out2 +=String.valueOf(1);
		out2+="\n";
		if  (musicmuted) out2 +=String.valueOf(0); else out2 +=String.valueOf(1);
		out2+="\n";
		if  (game.hardcore) out2 +=String.valueOf(1); else out2 +=String.valueOf(0);
		config.writeString(out2,false);

	}

	private String get_random_quote(int scores) {
		if (scores == Answer) {return "Answer: Tetrasnakonoid";}
		int total_win_quotes = 10;
		final String[] quotes = new String[total_win_quotes];
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

		int total_newfag_quotes = 11;
		final String[] newfag_quotes = new String[total_newfag_quotes];
		newfag_quotes[0] = motto;
		newfag_quotes[1] = "Rank: Puppy";
		newfag_quotes[2] = "Rank: Kitten";
		newfag_quotes[3] = "Rank: Newbie";
		newfag_quotes[4] = "Rank: Slowpoke";
		newfag_quotes[5] = "Try Harder";
		newfag_quotes[6] = "No, John. You are the Tetrasnakonoid";
		newfag_quotes[7] = "AHAHAHAHAHAAHAHAH";
		newfag_quotes[8] = "You Are Already Dead";
		newfag_quotes[9] = "All Your Base Are Belong To Us";
		newfag_quotes[10] = "The Game";

		int total_easy_quotes = 10;
		final String[] easy_quotes = new String[total_easy_quotes];
		easy_quotes[0] = "Try hard, go hardcore.";
		easy_quotes[1] = "Easy mode is for kids.";
		easy_quotes[2] = "Are you 12?";
		easy_quotes[3] = "Easy mode. AHAHAHAHAHAAHAHAH.";
		easy_quotes[4] = "Too easy.";
		easy_quotes[5] = "Hardcore is the answer.";
		easy_quotes[6] = "Special easy mode. Even if you win...";
		easy_quotes[7] = "EASY! EASY! EASY!";
		easy_quotes[8] = "Skip tutorial.";
		easy_quotes[9] = "Casuals. Casuals never changes.";
		Random r = new Random();

		if (!game.hardcore) {
			if ( (scores) > 299 ) {return "What the hell are you doing here?";} else {
				return easy_quotes[r.nextInt(total_easy_quotes)];
			}
		}

		if (scores>300) return quotes[r.nextInt(total_win_quotes)]; else return newfag_quotes[r.nextInt(total_newfag_quotes)];
	}


	private void game_over(int source){
		if (DEBUG) {Gdx.app.debug(LOG_TAG, "Gameover reason: " + source);}

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

		if (!(Gdx.app.getType() == Application.ApplicationType.WebGL))
		save_cookies();

		explosion.start();

		game_over_flag = true;
		last_game_over_time = TimeUtils.millis();

	}

	private void recalc_tile_widths()
	{
		if (game.hardcore) {
			game.snake_h_tiles = TetrasnakonoidGame.snake_h_tiles_hardcore;
			game.snake_w_tiles = TetrasnakonoidGame.snake_w_tiles_hardcore;

			game.tetris_h_tiles = TetrasnakonoidGame.tetris_h_tiles_hardcore;
			game.tetris_w_tiles = TetrasnakonoidGame.tetris_w_tiles_hardcore;

		}
		else {
			game.snake_h_tiles = TetrasnakonoidGame.snake_h_tiles_easy;
			game.snake_w_tiles = TetrasnakonoidGame.snake_w_tiles_easy;

			game.tetris_h_tiles = TetrasnakonoidGame.tetris_h_tiles_easy;
			game.tetris_w_tiles = TetrasnakonoidGame.tetris_w_tiles_easy;
		}
		game.easy_tetris_rect.x = game.tile_size_px*TetrasnakonoidGame.max_width_tiles/2;
		game.easy_tetris_rect.y = game.tile_size_px*TetrasnakonoidGame.max_height_tiles/2;
		game.easy_tetris_rect.h = game.tile_size_px*TetrasnakonoidGame.max_height_tiles/2;
		game.easy_tetris_rect.w = game.tile_size_px*TetrasnakonoidGame.tetris_w_tiles_easy/2;
	}
	private void new_game() {
		if (DEBUG) {
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
		}

		if (first_try) game_state = 5; else game_state = 1;
		game.scores = 0;
		game_over_flag = false;

		set_random_colors();
		place_walls();
		recalc_tile_widths();
		game.easy_mode_state = 0;
		init_arcanoid();
		init_tetris();
		init_snake();

		game.has_lulz_ball = false;
		if (game_state == 1) {
			if (game.hardcore)
				Gdx.input.setInputProcessor(ingame);
			else
				Gdx.input.setInputProcessor(ingameeasy);
		}
		if (game_state == 5)
			Gdx.input.setInputProcessor(null);

		last_render = TimeUtils.millis();
		last_new_game_time = last_render;

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
		spr_help_overlay = new Sprite(help_overlay);
		spr_easy_help_overlay = new Sprite(easy_help_overlay);
		drw_help_quit = new TextureRegionDrawable(new TextureRegion(help_quit));

		btn_help_quit = new ImageButton(drw_help_quit);
		btn_help_quit.setPosition(0,0);


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
		explosion = new ParticleEffect();
		explosion.load(Gdx.files.internal("explosion.particle"),Gdx.files.internal(""));
		explosion.getEmitters().first().setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
		explosion.scaleEffect(3.0f);


		drw_game_over_back = new TextureRegionDrawable(new TextureRegion(game_over_quit));
		btn_game_over_back = new ImageButton(drw_game_over_back);
		btn_game_over_back.setPosition(0,0);

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
		logo = new Image(drw_logo);

		hardcoremark = new Sprite(hardcoredown);
		hardcoremark.setAlpha(0.5f);

		gameover = new Stage(viewport);
		game_over_layout = new Table();

		gameover.addActor(btn_game_over_back);
		gameover.addActor(game_over_layout);

		btn_game_over_back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (TimeUtils.millis() - last_game_over_time > 2000) {
					first_try = false;
					game_state = 0;
					Gdx.input.setInputProcessor(ui);
				}
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
		                 "Consider a small beer donation\n" +
						 "So I can haz motivation to polish it further. See ya. \n" +
						 "P.S. Visit " + donateURI+ " for donations.\n" +
						 "Use "+ githubURI + "\n" +
		                 "or " +mailto + " for feedback.";

		Label.LabelStyle credits_style = new Label.LabelStyle();
		credits_style.font = scorefont;
		credits_style.fontColor = Color.BLACK;
		lbl_credits = new Label(str_credits, credits_style);

		drw_credits_quit = new TextureRegionDrawable(new TextureRegion(credits_quit));
		btn_credits_quit = new ImageButton(drw_credits_quit);
		btn_credits_quit.setPosition(0,0);



		credits = new Stage(viewport);
		credits_layout = new Table();



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

	private void tetramino_move_left()
	{
		if (game.tetramino_x == 0) return;
		boolean left = true;
		boolean flag = check_tetramino_move(left);
		if (flag) game.tetramino_x--;
	}

	private void tetramino_move_right()
	{
		int width = get_tetramino_width(game.tetramino_type, game.tetramino_rot);
		if (width + game.tetramino_x > game.tetris_w_tiles - 1) return;

		boolean flag = check_tetramino_move(false);
		if ((flag))
			game.tetramino_x++;
	}


	private void easy_rack_up()
	{
		int max = game.vp_h - game.pc.bb.h;
		int delta = 2*game.pc.bb.h;
		if (game.pc_pitcher) {
			game.ai.target_y += delta;
			if (game.ai.target_y>= max) game.ai.target_y = max;
		}
		else {
			game.pc.target_y += delta;
			if (game.pc.target_y>= max) game.pc.target_y = max;
		}
	}

	private void easy_rack_down()
	{
		int min = game.pc.bb.h;
		int delta = 2*game.pc.bb.h;
		if (game.pc_pitcher) {
			game.ai.target_y -= delta;
			if (game.ai.target_y<= min) game.ai.target_y = min;

		}
		else {
			game.pc.target_y -= delta;
			if (game.pc.target_y<= min) game.pc.target_y = min;
		}
	}
	private void init_ingame() {
		ingame = new Stage(viewport);
		detect_tile_size(Gdx.graphics.getHeight(),Gdx.graphics.getWidth());
		shapeRenderer = new ShapeRenderer();

		drw_rack = new TextureRegionDrawable(new TextureRegion(rack_control));
		btn_rack = new ImageButton(drw_rack);
		btn_rack.setPosition(0,0);

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

		drw_snake_ctll = new TextureRegionDrawable(new TextureRegion(snake_control));
		btn_snakel = new ImageButton(drw_snake_ctll);



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
			}
		});

		btn_snakel.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (y >= btn_snakel.getHeight()/2) {
					game.next_tetramino_dir = -1;
					return true;
				}
				return false;
			}
		});


		btn_snakel.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				game.next_tetramino_dir = 0;

			}
		});


		drw_snake_ctlr = new TextureRegionDrawable(new TextureRegion(snake_control));
		btn_snaker = new ImageButton(drw_snake_ctlr);



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

			}
		});

		btn_snaker.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (y >= btn_snaker.getHeight()/2) {
					game.next_tetramino_dir = 1;
					return true;
				}
				return false;
			}
		});


		btn_snaker.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				game.next_tetramino_dir = 0;
			}
		});

		drw_tetr_move = new TextureRegionDrawable(new TextureRegion(tetr_move));
		btn_tetr_move = new ImageButton(drw_tetr_move);
		btn_tetr_move.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				int orientation = game.snake_directions.getFirst();
				if (y<Gdx.graphics.getHeight()/2) {
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



		btn_tetr_rot.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				if (check_tetramino_rot()) next_tetr_rot();
			}});


		drw_gamepadup = new TextureRegionDrawable(new TextureRegion(gamepad_up));
		btn_gamepadup = new ImageButton(drw_gamepadup);

		drw_gamepaddown = new TextureRegionDrawable(new TextureRegion(gamepad_down));
		btn_gamepaddown = new ImageButton(drw_gamepaddown);

		drw_gamepadleft = new TextureRegionDrawable(new TextureRegion(gamepad_left));
		btn_gamepadleft = new ImageButton(drw_gamepadleft);

		drw_gamepadright = new TextureRegionDrawable(new TextureRegion(gamepad_right));
		btn_gamepadright = new ImageButton(drw_gamepadright);

		drw_gamepadrot = new TextureRegionDrawable(new TextureRegion(gamepad_rot));
		btn_gamepadrot = new ImageButton(drw_gamepadrot);

		btn_gamepadup.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (game.easy_mode_state <=1) {
					int orientation = game.snake_directions.getFirst();
					if ((orientation == 3) || (orientation == 4)) {
						game.next_snake_dir = 2;
					}
				}

				if (game.easy_mode_state>=2) {
					easy_rack_up();
				}
			}
		});

		btn_gamepaddown.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (game.easy_mode_state <=1) {
					int orientation = game.snake_directions.getFirst();
					if ((orientation == 3) || (orientation == 4)) {
						game.next_snake_dir = 1;
					}
				}

				if (game.easy_mode_state>=2) {
					easy_rack_down();

				}
			}
		});

		btn_gamepadleft.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (game.easy_mode_state <=1) {
					int orientation = game.snake_directions.getFirst();
					if ((orientation == 1) || (orientation == 2)) {
						game.next_snake_dir = 3;
						return false;
					}
				}
				if (game.easy_mode_state==2) {
					game.next_tetramino_dir = -1;
					return true;
				}
				return false;
			}
		});


		btn_gamepadleft.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (game.easy_mode_state==2) {
					game.next_tetramino_dir = 0;
				}
			}
		});

		btn_gamepadright.addListener(new ClickListener() {
			@Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (game.easy_mode_state <=1) {
					int orientation = game.snake_directions.getFirst();
					/* Vertical */
						if ((orientation == 1) || (orientation == 2)) {
							game.next_snake_dir = 4;
							return  false;
						}
				}

				if (game.easy_mode_state==2) {
					game.next_tetramino_dir = 1;
					return true;
				}
				return  false;
			}
		});

		btn_gamepadright.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (game.easy_mode_state==2) {
					game.next_tetramino_dir = 0;
				}
			}
		});

		btn_gamepadrot.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (game.easy_mode_state==2) {
					if (check_tetramino_rot()) next_tetr_rot();
				}
			}
		});

		ingame.addActor(btn_snakel);
		ingame.addActor(btn_snaker);
		ingame.addActor(btn_tetr_move);
		ingame.addActor(btn_tetr_rot);
		ingame.addActor(btn_rack);

		ingameeasy = new Stage(viewport);
		ingameeasy.addActor(btn_gamepadup);
		ingameeasy.addActor(btn_gamepaddown);
		ingameeasy.addActor(btn_gamepadleft);
		ingameeasy.addActor(btn_gamepadright);
		ingameeasy.addActor(btn_gamepadrot);
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

		drw_donate_usd = new TextureRegionDrawable(new TextureRegion(donateusd));
		btn_donate_usd = new ImageButton(drw_donate_usd);

		drw_hall_of_fame = new TextureRegionDrawable(new TextureRegion(halloffame));
		btn_halloffame = new ImageButton(drw_hall_of_fame);

		drw_ratemeup = new TextureRegionDrawable(new TextureRegion(ratemeup));
		drw_ratemedown = new TextureRegionDrawable(new TextureRegion(ratemedown));
		btn_rateme = new ImageButton(drw_ratemeup, drw_ratemedown);

		drw_hardcoreup = new TextureRegionDrawable(new TextureRegion(hardcoreup));
		drw_hardcoredown = new TextureRegionDrawable(new TextureRegion(hardcoredown));
		btn_hardcore = new ImageButton(drw_hardcoreup, drw_hardcoreup, drw_hardcoredown);
		btn_hardcore.setChecked(game.hardcore);

		drw_new_game = new TextureRegionDrawable(new TextureRegion(newgame));
		btn_new_game = new ImageButton(drw_new_game);

		drw_header = new TextureRegionDrawable(new TextureRegion(header));
		img_header = new Image(drw_header);

		spr_protip = new Sprite(protip);
		motto_y = 60 + (float) Math.random() * (Gdx.graphics.getHeight() - 2 * 60);


		scorefont = new BitmapFont(Gdx.files.internal("Inconsolata-LGC-48.fnt"));
		scorefont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		Label.LabelStyle score_style = new Label.LabelStyle();
		score_style.font = scorefont;
		score_style.fontColor = Color.BLACK;


		String lbl ="TOP: ";
		if (has_high_scores) {
			if (game.hardcore) {
				lbl += top_player_name;
				lbl += " ";
				lbl += String.valueOf(high_score);
			}
			else {
				lbl += top_player_name_easy;
				lbl += " ";
				lbl += String.valueOf(high_score_easy);
			}
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
		init_ingame();
		init_helpscreen();
		init_gameoverscreen();
		init_creditsscreen();
		ask_username = new TetrasnakonoidUsernameDialog();
		ui = new Stage(viewport);

		layout = new Table();



		btn_new_game.setPosition(0,0);
		ui.addActor(btn_new_game);
		ui.addActor(layout);
		ui.addActor(lbl_version);
		Gdx.input.setInputProcessor(ui);

		hardcorefire = new ParticleEffect();
		hardcorefire.load(Gdx.files.internal("hardcore.particle"),Gdx.files.internal(""));
		hardcorefire.scaleEffect(3.0f);

		btn_help.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				game_state = 2;
				spr_help_overlay.setAlpha(1.0f);
				spr_easy_help_overlay.setAlpha(1.0f);
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

		btn_donate_usd.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.net.openURI(donateUSDURI);
			}
		});

		btn_halloffame.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.net.openURI(halloffameURI);
			}
		});

		btn_rateme.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Gdx.app.getType() == Application.ApplicationType.iOS) {
					Gdx.net.openURI(ratemeURIApple);
				}
				else if (Gdx.app.getType() == Application.ApplicationType.Android)
				{
					Gdx.net.openURI(ratemeURIAndroid);
				}
				else {
					Gdx.net.openURI(ratemeURIOther);
				}
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
		btn_hardcore.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.hardcore =!game.hardcore;
				hardcorefire.getEmitters().first().setPosition(btn_hardcore.getX() + btn_hardcore.getWidth()/2, btn_hardcore.getY()+ btn_hardcore.getHeight()/2);
				String lbl1 = "TOP: ";
				String lbl2= "LAST: ";
				if (game.hardcore) {
					if (!first_try) {
						lbl2 += last_player_name;
						lbl2 += " ";
						lbl2 += String.valueOf(last_score);
						lbl_last_try_out.setText(lbl2);
					}
					lbl1 +=top_player_name;
					lbl1 +=" ";
					lbl1 += String.valueOf(high_score);
					lbl_high_scores_out.setText(lbl1);
				}
				else {
					if (!first_try) {
						lbl2 += last_player_name;
						lbl2 += " ";
						lbl2 += String.valueOf(last_score);
						lbl_last_try_out.setText(lbl2);
					}
					lbl1 +=top_player_name_easy;
					lbl1 +=" ";
					lbl1 += String.valueOf(high_score_easy);
					lbl_high_scores_out.setText(lbl1);
				}
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
		donateusd = new Texture(Gdx.files.internal("donateusd.png"));
		halloffame = new Texture(Gdx.files.internal("halloffame.png"));
		hardcoreup = new Texture(Gdx.files.internal("hardcoreup.png"));
		hardcoredown = new Texture(Gdx.files.internal("hardcoredown.png"));

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

		gamepad_up = new Texture(Gdx.files.internal("gamepad_up.png"));
		gamepad_down = new Texture(Gdx.files.internal("gamepad_down.png"));
		gamepad_left = new Texture(Gdx.files.internal("gamepad_left.png"));
		gamepad_right = new Texture(Gdx.files.internal("gamepad_right.png"));
		gamepad_rot = new Texture(Gdx.files.internal("gamepad_rot.png"));

		help_background = new Texture("help_background.png");
		help_overlay = new Texture("help_overlay.png");
		easy_help_overlay = new Texture("easy_help_overlay.png");
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

			if (out.length>=4) {
				top_player_name = out[0];
				high_score = Integer.parseInt(out[1]);
				top_player_name_easy = out[2];
				high_score_easy = Integer.parseInt(out[3]);

				has_high_scores = true;
			}
		}
		if (config.exists()) {
			String str_config = config.readString();
			String out[] = str_config.split("\\r?\\n");
			if (out.length>=4) {
				player_name = out[0];
				int sndm = Integer.parseInt(out[1]);
				int mscm = Integer.parseInt(out[2]);
				int hc = Integer.parseInt(out[3]);

				if (sndm == 1) sndmuted = false;
				else sndmuted = true;
				if (mscm == 1) musicmuted = false;
				else musicmuted = true;
				if (hc == 1) game.hardcore = true;
				else game.hardcore = false;
			}
		}
	}

	private void init_gameobjects() {
		game.pc = new Racket();
		game.pc.bb = new Rect();

		spr_pc_racket = new Sprite(racket, TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.racket_length_tiles * TetrasnakonoidGame.sprite_tile_size_px);
		spr_pc_racket.setOriginCenter();

		game.ai = new Racket();
		game.ai.bb = new Rect();
		spr_ai_racket = new Sprite(racket, TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.racket_length_tiles * TetrasnakonoidGame.sprite_tile_size_px);
		spr_ai_racket.setOriginCenter();


		game.ball = new Ball();
		game.false_ball = new Ball();
		game.lulz_ball = new Ball();

		spr_ball = new Sprite(ball_frames[0]);
		spr_ball.setOriginCenter();


		spr_lulz_ball = new Sprite(ball_frames[0]);
		spr_lulz_ball.setOriginCenter();


		spr_false_ball = new Sprite(ball_frames[0]);
		spr_false_ball.setOriginCenter();


		game.easy_tetris_rect = new Rect();
		int blockh = Math.max(TetrasnakonoidGame.tetris_h_tiles_easy, TetrasnakonoidGame.tetris_h_tiles_hardcore);
		int blockw = Math.max(TetrasnakonoidGame.tetris_w_tiles_easy, TetrasnakonoidGame.tetris_w_tiles_hardcore);
		game.blocks = new int[blockh+4][blockw+4];
		for (int i =0; i < blockh+4; ++i) {
			for (int j = 0; j < blockw + 4; ++j) {
				game.blocks[i][j] = 0;
			}
		}

		spr_tetrotile = new Sprite(tetrotile);
		spr_tetrotile.setOriginCenter();

		spr_tetroborderl = new Sprite(tetroborder);


		spr_tetroborderr = new Sprite(tetroborder);


		TextureRegion fr = new TextureRegion(food,TetrasnakonoidGame.sprite_tile_size_px*(game.food_id),0 , TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px);
		spr_food = new Sprite(fr);


		spr_head = new Sprite(head);

		spr_tail = new Sprite(tail);

		spr_body = new Sprite(body);

		spr_anglebody = new Sprite(anglebody);

	}

	private void fill_layouts()
	{
		game_over_layout.setFillParent(true);
		game_over_layout.setTransform(true);
		game_over_layout.add(logo).center().colspan(4).pad(10).spaceBottom(0.1f*Gdx.graphics.getHeight());
		game_over_layout.row();
		game_over_layout.add(lbl_scores_final).center().colspan(4).pad(10).spaceBottom(0.1f*Gdx.graphics.getHeight());
		game_over_layout.row();
		game_over_layout.add(lbl_quote_final).center().colspan(4).pad(10).spaceBottom(0.1f*Gdx.graphics.getHeight());
		game_over_layout.row().fillX();
		game_over_layout.add(btn_sharefb).pad(10);
		game_over_layout.add(btn_sharetw).pad(10);
		game_over_layout.add(btn_sharevk).pad(10);
		game_over_layout.add(btn_sharegh).pad(10);

		layout.setFillParent(true);
		layout.setTransform(true);
		layout.add(img_header).center().colspan(10).pad(10);
		layout.row();
		layout.add(lbl_high_scores_out).center().colspan(10).pad(30);
		layout.row();
		layout.add(lbl_last_try_out).center().colspan(10).pad(10);
		layout.row();
		layout.add(btn_hardcore).center().colspan(10).pad(30);
		layout.row().fillX();
		layout.add(btn_help).pad(10).width(Value.percentWidth(.07F, layout));
		layout.add(btn_mutesnd).pad(10).width(Value.percentWidth(.07F, layout));
		layout.add(btn_mutemusic).pad(10).width(Value.percentWidth(.07F, layout));
		layout.add(btn_login).pad(10).width(Value.percentWidth(.07F, layout));
		if (Gdx.app.getType().equals(Application.ApplicationType.WebGL)) {
			layout.add(btn_rateme).pad(10).width(Value.percentWidth(.07F, layout));
		}
		else {
			layout.add(btn_rateme).pad(10).spaceRight(0.1562f*Gdx.graphics.getWidth());
		}


		layout.add(btn_halloffame).pad(10).width(Value.percentWidth(.07F, layout));
		layout.add(btn_donate_usd).pad(10).width(Value.percentWidth(.07F, layout));
		layout.add(btn_donate_btc).pad(10).width(Value.percentWidth(.07F, layout));
		layout.add(btn_donate_ltc).pad(10).width(Value.percentWidth(.07F, layout));
		layout.add(btn_donate_eth).pad(10).width(Value.percentWidth(.07F, layout));

		credits_layout.setFillParent(true);
		credits_layout.setTransform(true);
		credits_layout.add(lbl_credits).center().colspan(1).pad(10);

	}

	@Override
	public void create() {
		if (DEBUG) Gdx.app.debug(LOG_TAG, "GDX ver: " + com.badlogic.gdx.Version.VERSION);

		if ( (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) ||
		     (Gdx.app.getType().equals(Application.ApplicationType.HeadlessDesktop)) )
		{
			Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width,Gdx.graphics.getDisplayMode().height);
		}

		if (Gdx.app.getType().equals(Application.ApplicationType.WebGL)) {
			Gdx.graphics.setWindowedMode(1280,720);
		}

		batch = new SpriteBatch();
		init_textures();
		init_music();
		game = new TetrasnakonoidGame();
		if (!(Gdx.app.getType() == Application.ApplicationType.WebGL)) { init_cookies(); }
		init_gui();
		init_animations();
		init_gameobjects();
		resize_everything(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		fill_layouts();
	}

	private void render_gui() {
		motto_x += Gdx.graphics.getDeltaTime() * motto_speed;
		if (motto_x > Gdx.graphics.getWidth()) {
			motto_x = 0;
			motto_y = 60 + (float) Math.random() * (Gdx.graphics.getHeight() - 2 * 60);
		}
		batch.begin();
		if (game.hardcore) {
			hardcorefire.getEmitters().first().setPosition(btn_hardcore.getX() + btn_hardcore.getWidth()/2, btn_hardcore.getY()+ btn_hardcore.getHeight()/2);
			hardcorefire.update(Gdx.graphics.getDeltaTime());
			hardcorefire.draw(batch);
		}
		batch.draw(spr_protip, (int) motto_x, (int) motto_y);
		batch.end();

		ui.draw();
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
		if (!game.hardcore)	rect_to_sprite(spr_false_ball, ball_to_rect(game.false_ball));

		ball_animation_acc += Gdx.graphics.getDeltaTime();
		br_angle+=360*Gdx.graphics.getDeltaTime(); if (br_angle>720) br_angle =0;

		if (ball_animation_acc> 0.1) {
			current_ball_frame++; if (current_ball_frame == total_ball_frames) current_ball_frame = 0;
			ball_animation_acc = 0;
			spr_ball.setRegion(new TextureRegion(ball,TetrasnakonoidGame.sprite_tile_size_px*current_ball_frame, 0, TetrasnakonoidGame.sprite_tile_size_px,TetrasnakonoidGame.sprite_tile_size_px));
			if (!game.hardcore)
			spr_false_ball.setRegion(new TextureRegion(ball,TetrasnakonoidGame.sprite_tile_size_px*current_ball_frame, 0, TetrasnakonoidGame.sprite_tile_size_px,TetrasnakonoidGame.sprite_tile_size_px));

			spr_ball.setOriginCenter();
			spr_ball.setRotation(br_angle);
			spr_false_ball.setOriginCenter();
			spr_false_ball.setRotation(br_angle);
		}

	}

	private void animate_ball() {
		if (game.hardcore || game.easy_mode_state > 0) {
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
		if (!game.hardcore && game.easy_mode_state == 0) {
			if (!game.pc_pitcher) {
				game.false_ball.y = game.pc.bb.y;
				game.ball.y = game.ai.bb.y;

			} else {
				game.false_ball.y = game.ai.bb.y;
				game.ball.y = game.pc.bb.y;
			}
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
        int tw = (TetrasnakonoidGame.max_width_tiles - game.tetris_w_tiles)/2;
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

	private void easy_snake_to_tetramino()
	{
		PointT a,b,c;
		a = index_to_snake_xy(0);
		b = index_to_snake_xy(1);
		c = index_to_snake_xy(2);

		int type,rot;
		type = 0; rot = 0;
		int type1[] = new int[]{1,4,5,6};
		int type2[] = new int[]{0,2,3,4,5,6};
		Random r = new Random();

		if ((a.x == b.x) && (b.x == c.x)) {
			type = type1[r.nextInt(type1.length)];
			if (type ==1) {
				rot  = 0;
			}
			else {
				if (r.nextInt()%2 == 0) {
					rot = 0;
				}
				else
				{
					rot = 2;
				}
			}
		}

		if ((a.y == b.y) && (b.y == c.y)) {
			type = type1[r.nextInt(type1.length)];
			if (type ==1) {
				rot  = 1;
			}
			else {
				if (r.nextInt()%2 == 0) {
					rot = 1;
				}
				else
				{
					rot = 3;
				}
			}
		}


		if	    (((a.x == c.x - 1) && (a.y == c.y - 1)) ||
				((a.x == c.x + 1) && (a.y == c.y + 1)) ||
				((a.x == c.x - 1) && (a.y == c.y + 1)) ||
				((a.x == c.x + 1) && (a.y == c.y - 1))) {
			type = type2[r.nextInt(type1.length)];
			if (type == 0) { rot = 0; }
			if ((type == 2) || (type == 3)) {rot = r.nextInt(2);}
			if (type>=4) {rot = r.nextInt(4);}
		}


		PointT p = snake_to_tetris(a);

		game.tetramino_type = type;
		game.tetramino_rot = rot;
		game.tetramino_x = p.x;
		game.tetramino_y = p.y;
		int w = get_tetramino_width(game.tetramino_type, game.tetramino_rot);
		if (game.tetramino_x + w >= game.tetris_w_tiles)
		{
			game.tetramino_x = game.tetris_w_tiles - w;
		}
		put_tetramino(game.tetramino_type, game.tetramino_rot,  game.tetramino_x, game.tetramino_y);

	}

	private void coll_resolve() {
		if (rect_circle(game.wall[0], ball_to_rect(game.ball))) { reflect_ball(game.ball, game.wall[0]); }
		if (rect_circle(game.wall[1], ball_to_rect(game.ball) )) { reflect_ball(game.ball, game.wall[1]);}
		if (rect_circle(game.wall[2], ball_to_rect(game.ball))) {
			if (game.hardcore) {
				game_over(0);
			}
			else {
				if (!game.pc_pitcher) {
					game_over(1);
				}
			}
		}
		if (rect_circle(game.wall[3], ball_to_rect(game.ball) )) {
			if (game.hardcore) {
				next_ball();
				game.scores += TetrasnakonoidGame.super_scores;
				jingle_jingle();
			}
			else {
				if (game.pc_pitcher) { game_over(2);}
			}
		}

		if (rect_circle(game.ai.bb,ball_to_rect(game.ball))) {
			if (game.hardcore) {
				reflect_ball(game.ball, game.ai.bb);
				game.pc_turn = true;
			}
			else {
				if (game.pc_pitcher) {
					game.kicked_back = true;
					reflect_ball(game.ball, game.ai.bb);
					jingle_jingle();
				}
			}
		}
		if (rect_circle(game.pc.bb,ball_to_rect(game.ball))) {
			if (game.hardcore) {
				reflect_ball(game.ball, game.pc.bb);
				if (game.pc_turn) game.scores++;
				game.pc_turn = false;
				jingle_jingle();
			}
			else {
				if (!game.pc_pitcher) {
					game.kicked_back = true;
					reflect_ball(game.ball, game.pc.bb);
					jingle_jingle();
				}
			}
		}

		if (rect_rect(game.ai.bb, snake_to_rect(game.headx, game.heady))) { game_over(3);}
		if (rect_rect(game.pc.bb, snake_to_rect(game.headx, game.heady))) { game_over(4);}

		if (game.hardcore) {
			for (int i = 1; i < game.s_length; ++i) {
				PointT p = index_to_snake_xy(i);
				Rect r = snake_to_rect(p.x, p.y);
				if (rect_rect(game.ai.bb, r) ||
						rect_rect(game.pc.bb, r) ||
						rect_circle(r, ball_to_rect(game.ball))
						) {
					if (i < TetrasnakonoidGame.snake_init_length_tiles) {
						for (int j = TetrasnakonoidGame.snake_init_length_tiles; j < game.s_length; ++j)
							game.snake_directions.removeLast();
						game.s_length = TetrasnakonoidGame.snake_init_length_tiles;
					} else {
						for (int j = i; j < game.s_length; ++j) game.snake_directions.removeLast();
						game.s_length = i;
						break;

					}
				}
			}
		}
		else {
			for (int i = 0; i < TetrasnakonoidGame.snake_init_length_tiles_easy; ++i) {
				PointT p = index_to_snake_xy(i);
				Rect r = snake_to_rect(p.x, p.y);
				if ((rect_circle(r, ball_to_rect(game.ball))) && (game.easy_mode_state == 1)) {
				    game.outs_in_a_row = 0;
					easy_snake_to_tetramino();
					if (game.ball.y > game.vp_h/2) { game.tetris_reverse_gravity = false; } else {game.tetris_reverse_gravity = true;}
					next_food_easy();
					change_ball_course();
					jingle_jingle();
					game.easy_mode_state = 2;
				}
			}

			if (rect_rect(ball_to_rect(game.ball), game.easy_tetris_rect ) &&(game.easy_mode_state==1)) { game.pitch_out = true; }

			int x = (TetrasnakonoidGame.max_width_tiles - TetrasnakonoidGame.tetris_w_tiles_easy)/2;
			if (game.pc_pitcher) {
				if ((game.ball.x > (x+TetrasnakonoidGame.tetris_w_tiles_easy+2)*game.tile_size_px) && (game.easy_mode_state == 1)) {
					game_over(5);
				}

				if ((game.pitch_out) && (game.ball.x < (x-3)*game.tile_size_px)) {
				    game.outs_in_a_row++; if (game.outs_in_a_row>11) game_over(17);
					next_pitch();
				}
			}
			else {
				if ((game.ball.x < (x-2)*game.tile_size_px) && (game.easy_mode_state == 1)) {
					game_over(6);
				}

				if ((game.pitch_out) && (game.ball.x > (x+3+TetrasnakonoidGame.tetris_w_tiles_easy)*game.tile_size_px)) {
                    game.outs_in_a_row++; if (game.outs_in_a_row>11) game_over(17);
					next_pitch();
				}
			}





		}
		if (game.hardcore) {
			int width = get_tetramino_width(game.tetramino_type, game.tetramino_rot);
			int height = get_tetramino_height(game.tetramino_type, game.tetramino_rot);
			for (int i = game.tetramino_y; i < game.tetramino_y + height; ++i) {
				for (int j = game.tetramino_x; j < game.tetramino_x + width; ++j) {
					if (has_block(i, j)) {
						if (rect_circle(block_to_rect(j, i), ball_to_rect(game.ball))) {
							clear_tetramino();
							next_tetramino();
						}
					}
				}
			}
		}
		for (int i =0; i< game.tetris_h_tiles;++i) {
			for (int j = 0; j< game.tetris_w_tiles;++j) {
				if (has_block(i,j)) {
					if (rect_circle(block_to_rect(j, i), ball_to_rect(game.ball))) {
						if ((game.hardcore) || (game.easy_mode_state <2))
							reflect_ball(game.ball, block_to_rect(j,i));
					}
				}
			}
		}


		if (!game.hardcore) {
			int width = get_tetramino_width(game.tetramino_type, game.tetramino_rot);
			int height = get_tetramino_height(game.tetramino_type, game.tetramino_rot);
			for (int i = game.tetramino_y; i < game.tetramino_y + height; ++i) {
				for (int j = game.tetramino_x; j < game.tetramino_x + width; ++j) {
					if (has_block(i, j)) {
						if (rect_circle(block_to_rect(j, i), snake_to_rect(game.foodx,game.foody))) {
							game.got_food = true; jingle_jingle();
						}
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
	private PointT snake_to_tetris(PointT in) {
		PointT out = new PointT(0,0);
		out.y = in.y;
		out.x = in.x - (TetrasnakonoidGame.max_width_tiles - game.tetris_w_tiles)/2;
		return out;
	}
	private void snake_to_block()
	{
		PointT p;
		for (int i = 0; i<game.s_length; i++) {
			p = snake_to_tetris(index_to_snake_xy(i));
			if ((p.x) >= 0 && (p.x<game.tetris_w_tiles))
			game.blocks[p.y][p.x] = 1;
		}
	}


	private void check_snake_collisions() {
		if (game.hardcore) {
			int width = get_tetramino_width(game.tetramino_type, game.tetramino_rot);
			int height = get_tetramino_height(game.tetramino_type, game.tetramino_rot);
			for (int i = game.tetramino_y; i < game.tetramino_y + height; ++i) {
				for (int j = game.tetramino_x; j < game.tetramino_x + width; ++j) {
					if (has_block(i, j)) {
						if (rect_rect(snake_to_rect(game.headx, game.heady), block_to_rect(j, i))) {
							if (game.s_length <= TetrasnakonoidGame.snake_init_length_tiles) {
								game.s_length = TetrasnakonoidGame.snake_init_length_tiles;
								snake_to_block();
								game_over(7);
							}

							clear_tetramino();
							next_tetramino();
							game.scores += TetrasnakonoidGame.super_scores;
							jingle_jingle();
							game.s_length--;
							game.snake_directions.removeLast();

						}
					}
				}
			}
			for (int i = game.tetramino_y; i < game.tetramino_y + height; ++i) {
				for (int j = game.tetramino_x; j < game.tetramino_x + width; ++j) {
					for (int k = 1; k < game.s_length; k++) {
						PointT p = index_to_snake_xy(k);
						Rect r = snake_to_rect(p.x, p.y);
						if ((has_block(i, j)) && (rect_rect(block_to_rect(j, i), r))) {
							if (k < TetrasnakonoidGame.snake_init_length_tiles) {
								for (int l = TetrasnakonoidGame.snake_init_length_tiles; l < game.s_length; ++l)
									game.snake_directions.removeLast();
								game.s_length = TetrasnakonoidGame.snake_init_length_tiles;
							} else {
								for (int l = k; l < game.s_length; ++l)
									game.snake_directions.removeLast();
								game.s_length = k;
								break;
							}
						}
					}
				}
			}
		}
		for (int i =0; i< game.tetris_h_tiles;++i) {
			for (int j=0; j<game.tetris_w_tiles;++j) {
				if (game.blocks[i][j] == 1) {
					if (rect_rect(snake_to_rect(game.headx, game.heady), block_to_rect(j, i))) {
						if ((game.hardcore) || (game.easy_mode_state <=1))
						game_over(8);
					}
				}
			}
		}

		for (int i=1; i<game.s_length; ++i) {
			PointT p =  index_to_snake_xy(i);
			if ((p.x == game.headx) &&
					(p.y == game.heady)){
				game_over(9);
			}
		}

		if (game.hardcore) {
			if (rect_rect(snake_to_rect(game.foodx, game.foody), ball_to_rect(game.ball))){
				next_lulz_ball();
			}


			if (game.snake_hunger < game.tile_size_px / 2) {
				game.s_length--;
				game.snake_directions.removeLast();
				if (game.s_length <= TetrasnakonoidGame.snake_init_length_tiles) {
					game_over(10);
				}
			}
		}
	}
	private void recalc_game_difficulty()
	{
	    if (game.hardcore) {
            int k = game.scores / 50;
            game.difficulty_a = k;
            game.difficulty_s = k;
            game.difficulty_t = k;
        }
        else {
            int k = game.scores / 5;
            game.difficulty_a = k;
            game.difficulty_s = k;
            game.difficulty_t = k;
        }
	}
	private void animate_snake(){
		game.snake_ani_acc+=Gdx.graphics.getDeltaTime();
		int speed = TetrasnakonoidGame.snake_speed_tiles_per_sec + game.difficulty_s;

		game.snake_hunger-=2*game.tile_size_px*(1+game.difficulty_s)*Gdx.graphics.getDeltaTime();
		if (game.snake_ani_acc > 1/(float)speed) {
			recalc_game_difficulty();

			check_snake_collisions();
			if (game.next_snake_dir > 0) {
				game.snake_directions.set(0,game.next_snake_dir);
				game.next_snake_dir = 0;
			}

			int a,b;
			if (game.hardcore) {
				a = 0;
				b = TetrasnakonoidGame.max_width_tiles-1;
			}
			else
			{
				a =(TetrasnakonoidGame.max_width_tiles - game.tetris_w_tiles)/2;
				b = a + TetrasnakonoidGame.tetris_w_tiles_easy - 1;
			}

		    int e = game.snake_directions.get(0);
			switch (e)
			{
				case 1:
					 if (game.heady <= 0) game.heady = TetrasnakonoidGame.max_height_tiles - 1; else game.heady--; break;
				case 2:
					 if (game.heady >= TetrasnakonoidGame.max_height_tiles-1) game.heady = 0; else 	game.heady++; break;
				case 3:
					 if (game.headx <= a) game.headx = b; else game.headx--;
					break;
				case 4:
					 if (game.headx >= b) game.headx = a; else  game.headx++;
					break;
				default:
					break;

			}
			game.snake_directions.addFirst(e);
			if ((game.headx == game.foodx) && (game.heady == game.foody)) {
				if (game.hardcore) {next_food(); game.s_length++; game.scores++; jingle_jingle();}
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

	private void check_easy_win() {
		if ((!game.hardcore) && (game.tetramino_landed) && (game.kicked_back)) {

			if (game.got_food) {
				game.scores+=3;
			}
			else {
				game.scores++;
			}
			next_pitch();
		}
	}

	private void rack_ai() {
		int max = game.vp_h - game.ai.bb.h;
		int min = game.ai.bb.h;
		game.rack_ai_acc += Gdx.graphics.getDeltaTime();
		if (game.hardcore) {
			boolean roll = false;
			if (game.rack_ai_acc > 0.3f) {
				Random r = new Random();
				if (r.nextInt() % 3 == 1) {
					roll = true;
				}
				if (game.ball.vx < 0) {
					if (roll)
						game.ai.target_y = min + (int) (Math.random() * ((max - min) + 1));

				} else {
					if (game.ball.x > game.vp_w / 3) {
						int x2 = game.ball.x + (int) (2 * game.ball.vx);
						int y2 = game.ball.y + (int) (2 * game.ball.vy);
						PointT p = lines_intersect(game.ball.x, game.ball.y, x2, y2, game.vp_w, 20, game.vp_w, game.vp_h);

						if (p.flag == 2) {
							game.ai.target_y = game.vp_h / 2;
							if ((p.y < max) && (p.y > min)) game.ai.target_y = p.y;

							if ((p.y > max) && (p.y - max > game.vp_h / 2)) game.ai.target_y = min;
							if ((p.y < min) && (Math.abs(p.y - min) > game.vp_h / 2))
								game.ai.target_y = max;
						}
					} else {
						if (roll)
							game.ai.target_y = min + (int) (Math.random() * ((max - min) + 1));
					}
				}
				game.rack_ai_acc = 0.0f;
			}
		}
		else {
			if (game.rack_ai_acc > 0.3f) {
				if (game.easy_mode_state == 0) {
					game.ai.target_y = min + (int) (Math.random() * ((max - min) + 1));
					game.pc.target_y = min + (int) (Math.random() * ((max - min) + 1));
				}
				game.rack_ai_acc = 0.0f;
			}

			if (game.easy_mode_state == 0) {
				game.pitch_acc += Gdx.graphics.getDeltaTime();
				if (game.pitch_acc >= game.pitch_delay) {game.easy_mode_state = 1; game.pitch_acc =0;}
			}
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
		int a,b;
		if (game.hardcore) {
			a = 0;
			b = TetrasnakonoidGame.max_width_tiles-1;
		}
		else
		{
			a =(TetrasnakonoidGame.max_width_tiles - game.tetris_w_tiles)/2;
			b = a + TetrasnakonoidGame.tetris_w_tiles_easy - 1;
		}
		PointT out = new PointT(game.headx,game.heady);
		if (id == 0) return out;
		for (int i =1;i<=id; ++i) {
			switch (game.snake_directions.get(i)){
				case 1:
					if (out.y >= TetrasnakonoidGame.max_height_tiles - 1) out.y = 0; else out.y++ ; break;
				case 2:
					if (out.y <= 0) out.y = TetrasnakonoidGame.max_height_tiles - 1; else out.y--; break;
				case 3:
					if (out.x >= b) out.x = a; else out.x++;
					break;
				case 4:
					if (out.x <= a ) out.x = b; else out.x--;
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

		clear_tetramino();
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
		if (get_tetramino_width(game.tetramino_type,game.tetramino_rot) + game.tetramino_x > game.tetris_w_tiles) {prev_tetr_rot(); return false;}
		clear_tetramino();
		put_tetramino(game.tetramino_type, game.tetramino_rot, game.tetramino_x, game.tetramino_y);
		boolean flag = check_tetramino();
		unput_tetramino();
		clear_tetramino();

		prev_tetr_rot();

		put_tetramino(game.tetramino_type, game.tetramino_rot, game.tetramino_x, game.tetramino_y);

		return !flag;
	}


	private boolean check_tetramino(){
		for (int i = 0; i < game.tetris_h_tiles; ++i ) {
			for (int j = 0; j < game.tetris_w_tiles; ++j ){
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
		for (int i =0; i<game.tetris_w_tiles;++i) {
			if (game.blocks[id][i] == 0) { flag = false; return flag; }
		}
		return flag;
	}

	private boolean check_line_empty(int id)
	{
		boolean flag = true;
		for (int i =0; i<game.tetris_w_tiles;++i) {
			if (game.blocks[id][i] == 1) { flag = false; return flag; }
		}
		return flag;
	}

	private int find_middle_line_index() {
		int mli1 = TetrasnakonoidGame.max_height_tiles/2;

		for (int i=0;i<mli1; ++i) {
			if (check_line_empty(i)) mli1 = i;
		}
		int mli2 = TetrasnakonoidGame.max_height_tiles/2;

		for (int i=TetrasnakonoidGame.max_height_tiles - 1; i>=mli2; --i) {
			if (check_line_empty(i)) mli2 = i;
		}

		return Math.min(mli1,mli2);
	}

	private boolean clear_lines()
	{
		boolean flag = true;
		int lineid = 0;
		for (int i=0;i< game.tetris_h_tiles;++i) {
			flag = check_line(i);
			if (flag) {lineid = i; break;}
		}
		if (flag) {
			for (int i =0; i<game.tetris_w_tiles;++i) {
				game.blocks[lineid][i] = 0;
			}
			if (game.hardcore) {
				for (int i = lineid; i < game.tetris_h_tiles - 1; ++i) {
					for (int j = 0; j < game.tetris_w_tiles; ++j) {
						game.blocks[i][j] = game.blocks[i + 1][j];
					}
				}
			}
			else {
				int mli = find_middle_line_index();
				if (game.tetris_reverse_gravity) {
					for (int i = mli; i < lineid - 1; ++i) {
						for (int j = 0; j < game.tetris_w_tiles; ++j) {
							game.blocks[i][j] = game.blocks[i - 1][j];
						}
					}
				}
				else {
					for (int i = lineid; i < mli - 1; ++i) {
						for (int j = 0; j < game.tetris_w_tiles; ++j) {
							game.blocks[i][j] = game.blocks[i + 1][j];
						}
					}
				}
			}
			if (game.hardcore)
				game.scores+=TetrasnakonoidGame.super_scores; jingle_jingle();
		}
		return flag;
	}

	private void tetr_inc() {
		if (game.hardcore) { game.tetramino_y++; return; }

		if (game.tetris_reverse_gravity) { game.tetramino_y--;} else {game.tetramino_y++;}
	}

	private void tetr_dec() {
		if (game.hardcore) { game.tetramino_y--; return; }

		if (game.tetris_reverse_gravity) { game.tetramino_y++;} else {game.tetramino_y--;}
	}

	private void check_easy_tetris_lose(){
		boolean flag = true;
		for (int i=0;i<game.tetris_w_tiles; i++) {
			flag = true;
			for (int j=0;j<game.tetris_h_tiles; j++) {
				if (game.blocks[j][i] == 0) flag = false;
			}
			if (flag) { game_over(11); return;}
		}
	}

	private boolean tetris_input_check()
	{

		if ((Gdx.app.getType() == Application.ApplicationType.iOS) ||
		   (Gdx.app.getType() == Application.ApplicationType.Android)) {
			return true;
		}
		else {
			if (game.hardcore) {
				return	Gdx.input.isKeyPressed(Input.Keys.Q) || Gdx.input.isKeyPressed(Input.Keys.E);
			}
			else {
				if (game.easy_mode_state >= 2) {
					return Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D);
				}
			}
		}
		return false;

	}

	private void animate_tetris(){
		game.tetris_ani_acc+=Gdx.graphics.getDeltaTime();
		int speed = game.tetris_speed_tiles_per_sec + game.difficulty_t;
		if (game.tetris_ani_acc > 1/(float) speed) {
			if ((game.hardcore) || (game.easy_mode_state==2)) {
				if ((game.next_tetramino_dir == -1) && (tetris_input_check())) {tetramino_move_left();}
				if ((game.next_tetramino_dir == 1) && (tetris_input_check())) {tetramino_move_right();}
				tetr_dec();
				clear_tetramino();

				put_tetramino(game.tetramino_type, game.tetramino_rot, game.tetramino_x, game.tetramino_y);

				if (check_tetramino()) {
					unput_tetramino();
					tetr_inc();
					put_tetramino(game.tetramino_type, game.tetramino_rot, game.tetramino_x, game.tetramino_y);
					tetramino_to_block();
					while (clear_lines()) {
						;
					}
					if (game.hardcore) {
						next_tetramino();
						game.scores++;
						jingle_jingle();
					}
					else {
						game.easy_mode_state = 3;
						game.tetramino_landed = true;
					}
				}
				if (game.tetramino_y == 0) {
					if (game.hardcore) {
						tetramino_to_block();
						while (clear_lines()) {
							;
						}
						next_tetramino();
						game.scores++;
						jingle_jingle();
					}
					else if (!game.tetris_reverse_gravity) {
						tetramino_to_block();
						while (clear_lines()) {
							;
						}
						game.easy_mode_state = 3;
						game.tetramino_landed = true;
					}
				}

				int height = get_tetramino_height(game.tetramino_type, game.tetramino_rot) + game.tetramino_y;
				if ((!game.hardcore) && (height == game.tetris_h_tiles) && (game.tetris_reverse_gravity))
				{
					tetramino_to_block();
					while (clear_lines()) {
						;
					}
					game.easy_mode_state = 3;
					game.tetramino_landed = true;
				}

				if (game.hardcore) {
					for (int i = 0; i < game.tetris_w_tiles; ++i) {
						if (game.blocks[game.tetris_h_tiles][i] == 1) game_over(15);
					}
				}
				else {
					check_easy_tetris_lose();
				}
			}
			game.tetris_ani_acc = 0.0f;

		}
	}

	private PointF block_index_to_sprite(int i, int j)
	{
		PointF p = new PointF();
		p.y = i*game.tile_size_px + game.vp_y;
		int x = (TetrasnakonoidGame.max_width_tiles - game.tetris_w_tiles)/2;
		p.x = j*game.tile_size_px + game.vp_x+x*game.tile_size_px;
		return p;
	}

	private void handlekbdinput() {
		if (Gdx.app.getType() == Application.ApplicationType.iOS) return;
		if (Gdx.app.getType() == Application.ApplicationType.Android) return;

		boolean tflag = false; if (game.tetris_ani_acc == 0) tflag = true;

		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			if (game.hardcore) {
				int orientation = game.snake_directions.getFirst();
				if ((orientation == 3) || (orientation == 4)) {
					game.next_snake_dir = 2;
				}
			}
			else {
				if (game.easy_mode_state <=1) {
					int orientation = game.snake_directions.getFirst();
					if ((orientation == 3) || (orientation == 4)) {
						game.next_snake_dir = 2;
					}
				}

				if (game.easy_mode_state>=2) {
					easy_rack_up();
				}
			}
		}

		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			if (game.hardcore) {
				int orientation = game.snake_directions.getFirst();
				if ((orientation == 3) || (orientation == 4)) {
					game.next_snake_dir = 1;
				}
			}
			else {
				if (game.easy_mode_state <=1) {
					int orientation = game.snake_directions.getFirst();
					if ((orientation == 3) || (orientation == 4)) {
						game.next_snake_dir = 1;
					}
				}

				if (game.easy_mode_state>=2) {
					easy_rack_down();
				}

			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			if (game.hardcore) {
				int orientation = game.snake_directions.getFirst();
				if ((orientation == 1) || (orientation == 2)) {
					game.next_snake_dir = 3;
				}
			}
			else {
				if (game.easy_mode_state <=1) {
					int orientation = game.snake_directions.getFirst();
					if ((orientation == 1) || (orientation == 2)) {
						game.next_snake_dir = 3;
					}
				}
				if (game.easy_mode_state==2) {
					game.next_tetramino_dir = -1;
				}
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			if (game.hardcore) {
				int orientation = game.snake_directions.getFirst();
				if ((orientation == 1) || (orientation == 2)) {
					game.next_snake_dir = 4;
				}
			}
			else {
				if (game.easy_mode_state <=1) {
					int orientation = game.snake_directions.getFirst();
					/* Vertical */
					if ((orientation == 1) || (orientation == 2)) {
						game.next_snake_dir = 4;
					}
				}

				if (game.easy_mode_state==2) {
					game.next_tetramino_dir = 1;
				}
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.Q)){
			if (game.hardcore) {
				game.next_tetramino_dir = -1;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.E)){
			if (game.hardcore) {
				game.next_tetramino_dir = 1;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.J)){
			if (game.hardcore) {
				int min = game.pc.bb.h;
				game.pc.target_y = min;

			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.K)){
			if (game.hardcore) {
				int max = game.vp_h - game.pc.bb.h;
				game.pc.target_y = max;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			if (game_state ==0) {
				if ((TimeUtils.millis() - desktop_state_timeout > 2000)) {
					if (!ask_username.username.equals("none")) player_name = ask_username.username;
					new_game();
				}
			}
			else {
				if (!game_over_flag && tflag) {
					if ((game.easy_mode_state == 2) || (game.hardcore)) {
						if (check_tetramino_rot()) next_tetr_rot();
					}
				}
			}

			if (game_over_flag) {
				if (TimeUtils.millis() - last_game_over_time > 2000) {
					first_try = false;
					game_state = 0;
					Gdx.input.setInputProcessor(ui);
					desktop_state_timeout=TimeUtils.millis();
				}
			}
		}
	}


	private  void render_game(boolean logic) {

		if (DEBUG) {
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.rect(game.vp_x, game.vp_y,  game.vp_w, game.vp_h);
			shapeRenderer.end();
		}

		if ((!game.hardcore) && (logic))
		ingameeasy.draw();

		Gdx.gl.glLineWidth(5);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(game.s_color);
		shapeRenderer.rect(game.vp_x, game.vp_y,  game.vp_w, game.vp_h);

		if (game.hardcore)
		{shapeRenderer.circle(spr_food.getX() + spr_food.getWidth()/2, spr_food.getY() + spr_food.getHeight()/2, game.snake_hunger);}

		shapeRenderer.end();
		Gdx.gl.glLineWidth(1);
		if (logic) {
			check_easy_win();
			rack_ai();
			coll_resolve();
			animate_ball();
			if (game.has_lulz_ball) animate_lulz_ball();
			animate_rack();
			animate_snake();
			animate_tetris();
			update_sprites();
		}

		if (first_try) {
			if (help_fadeout>0) {
				update_sprites();
				spr_help_overlay.setAlpha(help_fadeout);
				spr_easy_help_overlay.setAlpha(help_fadeout);
				help_fadeout -= 0.2*Gdx.graphics.getDeltaTime();
			}
			else {
				if (only_once) { if (game.hardcore) Gdx.input.setInputProcessor(ingame); else Gdx.input.setInputProcessor(ingameeasy);  game_state = 1; only_once = false; }
			}
		}

		batch.begin();
		spr_tetroborderl.draw(batch);
		spr_tetroborderr.draw(batch);
		spr_pc_racket.draw(batch);
		spr_ai_racket.draw(batch);
	    spr_ball.draw(batch);
        if (game.has_lulz_ball) spr_lulz_ball.draw(batch);
		if ((game.easy_mode_state == 0) && (!game.hardcore)) spr_false_ball.draw(batch);

		PointF p;
		PointT p2;
		if ((game.hardcore) || (game.easy_mode_state < 2)) {
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
		}
		if ((game.hardcore) || ((game.easy_mode_state == 2) && (!game.got_food))) {
			p = snake_to_render(game.foodx, game.foody);
			spr_food.setPosition(p.x, p.y);
			spr_food.draw(batch);
		}

		for (int i = 0; i < game.tetris_h_tiles; ++i)
			for (int j = 0; j < game.tetris_w_tiles; ++j){
				if ((game.blocks[i][j] == 1) || (game.blocks[i][j] == 2)) {
					p = block_index_to_sprite(i,j);
					spr_tetrotile.setPosition(p.x, p.y);
					spr_tetrotile.draw(batch);
				}
		}
		if ((first_try) && (help_fadeout>0)) { if (game.hardcore) spr_help_overlay.draw(batch); else spr_easy_help_overlay.draw(batch); }
	    batch.end();

		if (game.hardcore)
			ingame.draw();


		last_render = TimeUtils.millis();

	}
	private  void render_help() {
		batch.begin();
		spr_help_background.draw(batch);
		if (game.hardcore)
			spr_help_overlay.draw(batch);
		else
			spr_easy_help_overlay.draw(batch);
		batch.end();
		help.draw();
	}

	private  void render_gameover() {
		batch.begin();
		explosion.update(Gdx.graphics.getDeltaTime());
		explosion.draw(batch);
		if (game.hardcore) hardcoremark.draw(batch);
		batch.end();
		gameover.draw();

	}


	private  void render_credits() {
		credits.draw();
	}

	private void repos_everything (int w, int h) {
		int lbx = (TetrasnakonoidGame.max_width_tiles - game.tetris_w_tiles)/2;
		spr_tetroborderl.setPosition(lbx* game.tile_size_px + game.vp_x, game.vp_y);
		spr_tetroborderr.setPosition((lbx + game.tetris_w_tiles)  * game.tile_size_px + game.vp_x, game.vp_y);

		int scw = (game.vp_w - game.tile_size_px*4 - game.tile_size_px*game.tetris_w_tiles)/2;
		btn_snakel.setPosition(game.vp_x + game.tile_size_px*2,0);
		btn_snaker.setPosition(game.vp_x + game.tile_size_px*2+scw+ game.tile_size_px*game.tetris_w_tiles,0);

		btn_tetr_move.setPosition(lbx* game.tile_size_px + game.vp_x,0);
		btn_tetr_rot.setPosition( w - game.vp_x - game.tile_size_px*5,0);

		int gpx = TetrasnakonoidGame.max_width_tiles*game.tile_size_px - (3+3+1)*game.tile_size_px;
		int gpy = game.tile_size_px*(1+3);
		btn_gamepadup.setPosition(gpx, gpy + game.tile_size_px*3);
		btn_gamepaddown.setPosition(gpx, gpy - game.tile_size_px*3);
		btn_gamepadleft.setPosition(gpx - game.tile_size_px*3, gpy);
		btn_gamepadright.setPosition(gpx + game.tile_size_px*3, gpy);
		btn_gamepadrot.setPosition(game.tile_size_px*2, gpy + game.tile_size_px*3/2 - game.tile_size_px*5/2);

		hardcoremark.setPosition(w/2 - btn_hardcore.getWidth()/2, h/2 - btn_hardcore.getHeight()/2);
	}
	private void resize_everything(int w, int h) {
		if ((w<=0) || (h<=0)) return;

		detect_tile_size(h,w);
		float k = 0.0666f;
		btn_help.setSize(k*w,k*h);
		btn_mutesnd.setSize(k*w,k*h);
		btn_mutemusic.setSize(k*w,k*h);
		btn_login.setSize(k*w,k*h);
		btn_donate_btc.setSize(k*w,k*h);
		btn_donate_ltc.setSize(k*w,k*h);
		btn_donate_eth.setSize(k*w,k*h);
		btn_donate_usd.setSize(k*w,k*h);
		btn_halloffame.setSize(k*w,k*h);
		btn_rateme.setSize(k*w,k*h);

		btn_hardcore.setSize(0.5104f*w,0.1314f*h);

		btn_new_game.setSize(w,h);
		img_header.setSize(0.5171f*w, 0.2981f*h);

		spr_protip.setSize(0.221875f*w, 0.05185f*h);
		hardcorefire.scaleEffect(w/(float) favourite_width,h/(float)favourite_height,1.0f);

		lbl_high_scores_out.setFontScale(w/(float)favourite_width, h/(float)favourite_height);
		lbl_last_try_out.setFontScale(w/(float)favourite_width, h/(float)favourite_height);


		spr_help_background.setSize(w, h);
		spr_help_overlay.setSize(w, h);
		spr_easy_help_overlay.setSize(w, h);
		btn_help_quit.setSize(w, h);

		explosion.scaleEffect(w/(float) favourite_width,h/(float)favourite_height,1.0f);
		btn_game_over_back.setSize(w,h);

		btn_sharefb.setSize(k*w,k*h);
		btn_sharetw.setSize(k*w,k*h);
		btn_sharevk.setSize(k*w,k*h);
		btn_sharegh.setSize(k*w,k*h);

		lbl_scores_final.setFontScale(w/(float)favourite_width, h/(float)favourite_height);
		lbl_quote_final.setFontScale(w/(float)favourite_width, h/(float)favourite_height);
		hardcoremark.setSize(0.5104f*w,0.1314f*h);
		logo.setSize(0.5171f*w, 0.2981f*h);

		btn_credits_quit.setSize(w,h);
		lbl_credits.setFontScale(w/(float)favourite_width, h/(float)favourite_height);

		btn_rack.setSize(game.vp_x + game.tile_size_px*5, h);
		int scw = (game.vp_w - game.tile_size_px*4 - game.tile_size_px*game.tetris_w_tiles)/2;
		btn_snakel.setSize(scw, h);
		btn_snaker.setSize(scw, h);

		btn_tetr_move.setSize(game.tetris_w_tiles*game.tile_size_px, w);
		btn_tetr_rot.setSize(game.vp_x + game.tile_size_px*5, h);

		btn_gamepadup.setSize(game.tile_size_px*3, game.tile_size_px*3);
		btn_gamepaddown.setSize(game.tile_size_px*3, game.tile_size_px*3);

		btn_gamepadleft.setSize(game.tile_size_px*3, game.tile_size_px*3);

		btn_gamepadright.setSize(game.tile_size_px*3, game.tile_size_px*3);

		btn_gamepadrot.setSize(game.tile_size_px*5, game.tile_size_px*5);


		spr_pc_racket.setSize(1.0f*game.tile_size_px, TetrasnakonoidGame.racket_length_tiles * game.tile_size_px);
		spr_ai_racket.setSize(1.0f*game.tile_size_px, TetrasnakonoidGame.racket_length_tiles * game.tile_size_px);
		spr_ball.setSize(1.2f*game.k * TetrasnakonoidGame.sprite_tile_size_px, 1.2f*TetrasnakonoidGame.sprite_tile_size_px * game.k);
		spr_lulz_ball.setSize(1.2f*game.k * TetrasnakonoidGame.sprite_tile_size_px, 1.2f*TetrasnakonoidGame.sprite_tile_size_px * game.k);
		spr_false_ball.setSize(1.2f*game.k * TetrasnakonoidGame.sprite_tile_size_px, 1.2f*TetrasnakonoidGame.sprite_tile_size_px * game.k);
		spr_tetrotile.setSize(game.k * TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px * game.k); spr_head.setOriginCenter();
		spr_tetroborderr.setSize(5, game.vp_h);
		spr_tetroborderl.setSize(5, game.vp_h);

		spr_food.setSize(game.k * TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px * game.k);
		spr_head.setSize(game.k * TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px * game.k); spr_head.setOriginCenter();
		spr_tail.setSize(game.k * TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px * game.k); spr_tail.setOriginCenter();
		spr_body.setSize(game.k * TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px * game.k); spr_body.setOriginCenter();
		spr_anglebody.setSize(game.k * TetrasnakonoidGame.sprite_tile_size_px, TetrasnakonoidGame.sprite_tile_size_px * game.k); spr_anglebody.setOriginCenter();

		repos_everything(w,h);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render () {
			Gdx.gl.glClearColor(OLD_TETRIS_COLOR[0], OLD_TETRIS_COLOR[1], OLD_TETRIS_COLOR[2], 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			if (do_resize_next_frame) {resize_everything(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); do_resize_next_frame =false ;}
			handlekbdinput();
			switch (game_state) {
				case 0: render_gui();
					break;
				case 1:
					render_game(true);
					break;
				case 2:  render_help();
					break;
				case 3: render_credits();
					break;
				case 4:
					render_game(false);
					render_gameover();
					break;
				case 5:
					render_game(false);
					break;
				default:
					render_game(true);
					break;
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
		ingame.dispose();
		ingameeasy.dispose();
		explosion.dispose();

		helpup.dispose();
		helpdown.dispose();
		mutesnd.dispose();
		unmutesnd.dispose();
		mutemusic.dispose();
		unmutemusic.dispose();
		loginup.dispose();
		logindown.dispose();
		ratemeup.dispose();
		ratemedown.dispose();
		donatebtc.dispose();
		donateltc.dispose();
		donateeth.dispose();
		donateusd.dispose();
		halloffame.dispose();
		hardcoredown.dispose();
		hardcoreup.dispose();
		header.dispose();
		protip.dispose();
		newgame.dispose();
		hardcorefire.dispose();

		ball.dispose();
		racket.dispose();
		rack_control.dispose();

		food.dispose();
		head.dispose();
		tail.dispose();
		body.dispose();
		anglebody.dispose();
		snake_control.dispose();

		tetrotile.dispose();
		tetroborder.dispose();
		tetr_move.dispose();
		tetr_rot.dispose();

		gamepad_up.dispose();
		gamepad_down.dispose();
		gamepad_left.dispose();
		gamepad_right.dispose();
		gamepad_rot.dispose();

		help_background.dispose();
		help_overlay.dispose();
		easy_help_overlay.dispose();
		help_quit.dispose();

		sharefb.dispose();
		sharetw.dispose();
		sharevk.dispose();
		sharegh.dispose();
		game_over_quit.dispose();

		credits_quit.dispose();
	}
}

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
	/* Easy mode is for kids actually. Real men are HARDCORE ONLY or die trying.
	*  Real men don't do vidya? Huh? Fuck off friendo. THIS IS TETRASNAKONOID!!1111 */
	public boolean hardcore = false;
	public int easy_mode_state = 0; /* 0 - pitch preparation 1 - snake catch the ball 2 - tetris and rack*/
	public float pitch_delay = 1.5f;
	public float pitch_acc =0;
	public boolean pitch_out = false;

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
	public static final int racket_speed_tiles_sec = 7;
	Ball ball, lulz_ball, false_ball;
	int difficulty_a;
	public Color a_color;
	float rack_ai_acc;
	boolean has_lulz_ball;
	boolean pc_turn;
	boolean pc_pitcher;
	boolean kicked_back = false;
	public int outs_in_a_row = 0;

	public static final int snake_w_tiles_easy = 18;
	public static final int snake_h_tiles_easy = max_height_tiles;
	public static final int snake_w_tiles_hardcore = max_width_tiles;
	public static final int snake_h_tiles_hardcore = max_height_tiles;
	public int snake_w_tiles = 10;
	public int snake_h_tiles = 20;

	public static final int snake_init_length_tiles_easy = 3;
	public static final int snake_init_length_tiles = 4;
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
	public float snake_hunger = 1920;

	public static final int tetris_w_tiles_easy = 18;
	public static final int tetris_h_tiles_easy = 20;
	public static final int tetris_w_tiles_hardcore = 10;
	public static final int tetris_h_tiles_hardcore = 20;
	public Rect easy_tetris_rect;

	public int tetris_w_tiles = 10;
	public int tetris_h_tiles = 20;
	public int tetris_speed_tiles_per_sec = 5;
	int[][] blocks;
	int tetramino_type;
	int tetramino_rot;
	int tetramino_x;
	int tetramino_y;
	float tetris_ani_acc;
	public boolean tetris_reverse_gravity = false;
	public boolean got_food = false;
	public boolean tetramino_landed = false;
	public int next_tetramino_dir = 0;

	int difficulty_t;
	Color t_color;
	public static final int super_scores = 5;
	int scores;
}