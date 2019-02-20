package com.example.kevengao.godairy;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevengao.godairy.adapter.MainAdapter;
import com.example.kevengao.godairy.bean.SQLBean;
import com.example.kevengao.godairy.data.CallAlarm;
import com.example.kevengao.godairy.db.DatabaseOperation;
import com.example.kevengao.godairy.person.myhome;
import com.example.kevengao.godairy.view.MyGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {
	private LinearLayout bt_add;// 添加按钮
	private SQLiteDatabase db;//数据库对象
	private DatabaseOperation dop;//自定义数据库类
	private MyGridView lv_notes;// 消息列表
	private TextView tv_note_id, tv_locktype, tv_lock;
	public static MediaPlayer mediaPlayer;// 音乐播放器
	public static Vibrator vibrator;//手机震动器
	public AlarmManager am;// 消息管理者
	public MainAdapter adapter;
	LinearLayout llBtnMenu;
	PublishDailog pDialog;
	public ImageView write;
	private ImageView iv_wode;
	private ImageView iv_guanzhu;
	private ImageView iv_serch;
	private ImageView iv_dongtai;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//搜索功能
		iv_serch=(ImageView)findViewById(R.id.iv_serch);
		iv_serch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SearchActivity.class);
				startActivity(intent);

			}
		});
		bt_add = (LinearLayout) findViewById(R.id.bt_add);
		bt_add.setOnClickListener(new ClickEvent());

		iv_wode=(ImageView)findViewById(R.id.iv_wode);
		iv_wode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,myhome.class));
				finish();
			}
		});
 	iv_guanzhu=(ImageView)findViewById(R.id.iv_guanzhu);
 	iv_guanzhu.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			startActivity(new Intent(MainActivity.this,ContActivity.class));
			finish();
		}
	});
		iv_dongtai=(ImageView)findViewById(R.id.iv_dongtai);
		iv_dongtai.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,WeiboActivity.class));
			}
		});
		//弹出窗口
		llBtnMenu = (LinearLayout) findViewById(R.id.llBtnMenu);
		llBtnMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (pDialog == null) {
					pDialog = new PublishDailog(MainActivity.this);
					pDialog.setArticleBtnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {


						}
					});
					pDialog.setMiniBlogBtnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {

						}
					});
					pDialog.setPhotoBtnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
						}
					});
					pDialog.setLetterBtnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
                            //startActivity(new Intent(MainActivity.this,WeiboActivity.class));
							startActivity(new Intent(MainActivity.this,PaintActivity.class));
							Toast.makeText(MainActivity.this, "去涂鸦", Toast.LENGTH_LONG).show();
						}
					});
				}
				pDialog.show();
			}
		});

		// 数据库操作
		dop = new DatabaseOperation(this, db);
		lv_notes = (MyGridView) findViewById(R.id.lv_notes);
		if (am == null) {
			am = (AlarmManager) getSystemService(ALARM_SERVICE);
		}
		try {
			Intent intent = new Intent(MainActivity.this, CallAlarm.class);
			PendingIntent sender = PendingIntent.getBroadcast(
					MainActivity.this, 0, intent, 0);
			am.setRepeating(AlarmManager.RTC_WAKEUP, 0, 60 * 1000, sender);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// 显示记事列表
		showNotesList();
		// 为记事列表添加监听器
		lv_notes.setOnItemClickListener(new ItemClickEvent());
		// 为记事列表添加长按事件
		lv_notes.setOnItemLongClickListener(new ItemLongClickEvent());
	}

	// 显示记事列表
	private void showNotesList() {
		// 创建或打开数据库 获取数据
		dop.create_db();
		//获取数据库内容
		Cursor cursor = dop.query_db();
		if (cursor.getCount() > 0) {
			List<SQLBean> list = new ArrayList<SQLBean>();//日记信息集合里
			while (cursor.moveToNext()) {// 光标移动成功
				// 把数据取出
				SQLBean bean = new SQLBean();//创建数据库实体类
				//保存日记信息id到实体类
				bean.set_id("" + cursor.getInt(cursor.getColumnIndex("_id")));
				bean.setContext(cursor.getString(cursor
						.getColumnIndex("context")));//保存日记内容到实体类
				//保存日记标题到实体类
				bean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				//保存日记记录时间到实体类
				bean.setTime(cursor.getString(cursor.getColumnIndex("time")));
				bean.setDatatype(cursor.getString(cursor.getColumnIndex("datatype")));//保存日记是否设置提醒时间到实体类
				bean.setDatatime(cursor.getString(cursor.getColumnIndex("datatime")));//保存日记提醒时间到实体类
				bean.setLocktype(cursor.getString(cursor.getColumnIndex("locktype")));//保存日记是否设置了日记锁到实体类
				//保存日记锁秘密到实体类
				bean.setLock(cursor.getString(cursor.getColumnIndex("lock")));
				list.add(bean);//把保存日记信息实体类保存到日记信息集合里
			}
			//倒序显示数据
			Collections.reverse(list);
			adapter = new MainAdapter(list, this);//装载日记信息到首页
			lv_notes.setAdapter(adapter);//日记列表设置日记信息适配器
		}
		dop.close_db();//关闭数据库
	}

	// 记事列表长按监听器
	class ItemLongClickEvent implements AdapterView.OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id) {
			//初始化日记id保存控件
			tv_note_id = (TextView) view.findViewById(R.id.tv_note_id);
			//初始化是否添加日记锁保存控件
			tv_locktype = (TextView) view.findViewById(R.id.tv_locktype);
			//初始化日记锁秘密保存信息
			tv_lock = (TextView) view.findViewById(R.id.tv_lock);
			//获取控件上是否设置日记锁信息
			String locktype = tv_locktype.getText().toString();
			//获取控件上日记密码信息
			String lock = tv_lock.getText().toString();
			//获取控件上id信息转换成int类型
			int item_id = Integer.parseInt(tv_note_id.getText().toString());
			//弹出选择操作框方法
			simpleList(item_id, locktype, lock);
			return true;
		}
	}
	// 简单列表对话框，用于选择操作
	public void simpleList(final int item_id, final String locktype,
						   final String lock) {
		//实例化AlertDialog
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,
				R.drawable.icon);
		//设置弹窗标题
		alertDialogBuilder.setTitle("您是想？");
		//设置弹窗图片
		alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
		//设置弹窗选项内容
		alertDialogBuilder.setItems(R.array.itemOperation,
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
							// 编辑
							case 0:
								if ("0".equals(locktype)) {//判断是否添加了秘密锁0没有
									Intent intent = new Intent(MainActivity.this,
											AddActivity.class);//跳转到添加日记页
									intent.putExtra("editModel", "update");//传递编辑信息
									intent.putExtra("noteId", item_id);//传递id信息
									startActivity(intent);//开始跳转
								} else {//有秘密锁
									// 弹出输入密码框
									inputTitleDialog(lock, 0, item_id);
								}
								break;
							// 删除
							case 1:
								//if ("0".equals(locktype)) {// 判断是否是加密日记 0没有
							{       dop.create_db();// 打开数据库
									dop.delete_db(item_id);//删除数据
									dop.close_db();// 关闭数据库
									lv_notes.invalidate();// 刷新列表显示
									showNotesList();//显示日记列表信息
								}
								break;
						}
					}
				});
		alertDialogBuilder.create();//创造弹窗
		alertDialogBuilder.show();//显示弹窗
	}
	// 记事列表单击监听器
	class ItemClickEvent implements AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
			tv_note_id = (TextView) view.findViewById(R.id.tv_note_id);
			tv_locktype = (TextView) view.findViewById(R.id.tv_locktype);
			tv_lock = (TextView) view.findViewById(R.id.tv_lock);
			String locktype = tv_locktype.getText().toString();
			String lock = tv_lock.getText().toString();
			int item_id = Integer.parseInt(tv_note_id.getText().toString());
			if ("0".equals(locktype)) {
				Intent intent = new Intent(MainActivity.this, AddActivity.class);
				intent.putExtra("editModel", "update");
				intent.putExtra("noteId", item_id);
				startActivity(intent);
			} else {
				inputTitleDialog(lock, 0, item_id);
			}
		}
	}
	// 加密日记打开弹出的输入密码框
	public void inputTitleDialog(final String lock, final int idtype, final int item_id) {// 密码输入框
		final EditText inputServer = new EditText(this);
		inputServer.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		inputServer.setFocusable(true);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请输入密码").setView(inputServer)
				.setNegativeButton("取消", null);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				String inputName = inputServer.getText().toString();
				if ("".equals(inputName)) {
					Toast.makeText(MainActivity.this, "密码不能为空请重新输入！",
							Toast.LENGTH_LONG).show();
				} else {
					if (inputName.equals(lock)) {
						if (0 == idtype) {
							Intent intent = new Intent(MainActivity.this,
									AddActivity.class);
							intent.putExtra("editModel", "update");
							intent.putExtra("noteId", item_id);
							startActivity(intent);
						//} else if (1 == idtype) {
							dop.create_db();
							dop.delete_db(item_id);
							dop.close_db();
							// 刷新列表显示
							lv_notes.invalidate();
							showNotesList();
						}
					} else {
						Toast.makeText(MainActivity.this, "密码不对哦！",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		builder.show();
	}
//
	class ClickEvent implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.bt_add:
					Intent intent = new Intent(MainActivity.this, AddActivity.class);
					intent.putExtra("editModel", "newAdd");
					startActivity(intent);
			}
		}
	}

	// About页面
	public void onAbout(View v) {
		Intent intent = new Intent(MainActivity.this, About.class);
		startActivity(intent);
	}
}


