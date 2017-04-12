package com.xiumi.qirenbao;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xiumi.qirenbao.home.company.CompanyFragment;
import com.xiumi.qirenbao.home.expert.PeopleHomeFragmet;
import com.xiumi.qirenbao.home.partnership.CompanyHome;
import com.xiumi.qirenbao.login.LoginActivity;
import com.xiumi.qirenbao.message.company.MessageFragment;
import com.xiumi.qirenbao.message.expert.PeopMessageFragment;
import com.xiumi.qirenbao.message.partnership.PeopeleMessageFragment;
import com.xiumi.qirenbao.mine.company.MineFragment;
import com.xiumi.qirenbao.order.company.ProductFragment;
import com.xiumi.qirenbao.order.expert.PeopleSelfFragment;
import com.xiumi.qirenbao.order.partnership.PeopleProductFragment;
import com.xiumi.qirenbao.team.expert.MasterTeamFragment;
import com.xiumi.qirenbao.team.expert.PeopleTeamFragmet;
import com.xiumi.qirenbao.team.partnership.TeamFragment;
import com.xiumi.qirenbao.utils.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qianbailu on 2017/1/20.
 * 未登陆进入首页
 */
public class MainActivity extends FragmentActivity {
    @Bind(R.id.layout_nav)
    RadioGroup layoutNav;
    @Bind(R.id.layout_content)
    LinearLayout content;
    @Bind(R.id.menu_home)
    RadioButton menuHome;//企业和合伙人首页
    @Bind(R.id.export_home)
    RadioButton exportHome;//达人首页
    @Bind(R.id.export_product)
    RadioButton exportProduct;//达人订单
    @Bind(R.id.menu_product)
    RadioButton menuProduct;//企业和合伙人订单
    @Bind(R.id.menu_message)
    RadioButton menuMessage;//企业和合伙人消息
    @Bind(R.id.export_message)
    RadioButton exportMessage;//达人消息
    @Bind(R.id.menu_team)
    RadioButton menuTeam;//企业团队
    @Bind(R.id.menu_partner)
    RadioButton menuPartner;//合伙人团队
    @Bind(R.id.menu_export)
    RadioButton menuExport;//达人团队
    private CompanyFragment companyFragment;//企业的主页
    private PeopleHomeFragmet peopleHomeFragmet;//达人人主页
    private CompanyHome companyHome;//合伙首页
    private ProductFragment productFragment;//企业订单
    private PeopleSelfFragment peopleSelfFragment;//达人订单
    private PeopleProductFragment peopleProductFragment;//[合伙订单]
    private MessageFragment messageFragment;//企业消息
    private PeopeleMessageFragment peopeleMessageFragment;//合伙人消息
    private PeopMessageFragment peopMessageFragment;//达人消息
    private TeamFragment teamFragment;//[合伙]我的团队[成员]
    private MineFragment mineFragment;//企业我的
    private PeopleTeamFragmet peopleTeamFragmet;//[达人]我的团队-未加入团队
    private MasterTeamFragment masterTeamFragment;//[达人]我的团队
    private long mExitTime;
    public static String access_token, id, user_lv, team_id, company, description, class_id, work_title, name, avatar, growth_value, sex, work_duty, work_years, role;
    public static int chose = -1;
    public static boolean isOrder = false;
    public static MainActivity INSTANCE = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        INSTANCE = this;
        Log.e("chose", chose + "");
        if (chose == 1) {
            menuHome.setChecked(true);
            changeFragment(4);
            menuHome.setVisibility(View.VISIBLE);
            exportHome.setVisibility(View.GONE);
            exportProduct.setVisibility(View.GONE);
            menuProduct.setVisibility(View.VISIBLE);
            menuMessage.setVisibility(View.VISIBLE);
            exportMessage.setVisibility(View.GONE);
            menuTeam.setVisibility(View.VISIBLE);
            menuPartner.setVisibility(View.GONE);
            menuExport.setVisibility(View.GONE);
        } else if (chose == 2) {
            changeFragment(5);
            exportHome.setChecked(true);
            menuHome.setVisibility(View.GONE);
            exportHome.setVisibility(View.VISIBLE);
            exportProduct.setVisibility(View.VISIBLE);
            menuProduct.setVisibility(View.GONE);
            menuMessage.setVisibility(View.GONE);
            exportMessage.setVisibility(View.VISIBLE);
            menuTeam.setVisibility(View.GONE);
            menuPartner.setVisibility(View.GONE);
            menuExport.setVisibility(View.VISIBLE);
        } else if (chose == 3) {
            changeFragment(11);
            menuHome.setChecked(true);
            menuHome.setVisibility(View.VISIBLE);
            exportHome.setVisibility(View.GONE);
            exportProduct.setVisibility(View.GONE);
            menuProduct.setVisibility(View.VISIBLE);
            menuMessage.setVisibility(View.VISIBLE);
            exportMessage.setVisibility(View.GONE);
            menuTeam.setVisibility(View.GONE);
            menuPartner.setVisibility(View.VISIBLE);
            menuExport.setVisibility(View.GONE);
        } else {
            Intent intent = getIntent();
            //未登录状态进入首页
            Log.e("getIntent()", getIntent() + "");
            access_token = "Bearer " + intent.getStringExtra("access_token");
            id = intent.getStringExtra("id");
            name = intent.getStringExtra("name");
            chose = intent.getIntExtra("chose", -1);
            team_id = intent.getStringExtra("team_id");
            avatar = intent.getStringExtra("avatar");
            growth_value = intent.getStringExtra("growth_value");
            sex = intent.getStringExtra("sex");
            role = intent.getStringExtra("role");
            work_duty = intent.getStringExtra("work_duty");
            work_years = intent.getStringExtra("work_years");
            work_title = intent.getStringExtra("work_title");
            company = intent.getStringExtra("company");
            user_lv = intent.getStringExtra("user_lv");
            description = intent.getStringExtra("description");
            class_id = intent.getStringExtra("class_id");
            Log.e("chose", chose + "哈哈");
            if (chose == 1) {
                //2是达人1是企业主3是合伙人
                changeFragment(4);
                menuHome.setChecked(true);
                menuHome.setVisibility(View.VISIBLE);
                exportHome.setVisibility(View.GONE);
                exportProduct.setVisibility(View.GONE);
                menuProduct.setVisibility(View.VISIBLE);
                menuMessage.setVisibility(View.VISIBLE);
                exportMessage.setVisibility(View.GONE);
                menuTeam.setVisibility(View.VISIBLE);
                menuPartner.setVisibility(View.GONE);
                menuExport.setVisibility(View.GONE);
            } else if (chose == 2) {
                //要判断用户是否加入团队
                changeFragment(5);
                exportHome.setChecked(true);
                menuHome.setVisibility(View.GONE);
                exportHome.setVisibility(View.VISIBLE);
                exportProduct.setVisibility(View.VISIBLE);
                menuProduct.setVisibility(View.GONE);
                menuMessage.setVisibility(View.GONE);
                exportMessage.setVisibility(View.VISIBLE);
                menuTeam.setVisibility(View.GONE);
                menuPartner.setVisibility(View.GONE);
                menuExport.setVisibility(View.VISIBLE);
            } else if (chose == 3) {
                changeFragment(11);//合伙人
                menuHome.setChecked(true);
                menuHome.setVisibility(View.VISIBLE);
                exportHome.setVisibility(View.GONE);
                exportProduct.setVisibility(View.GONE);
                menuProduct.setVisibility(View.VISIBLE);
                menuMessage.setVisibility(View.VISIBLE);
                exportMessage.setVisibility(View.GONE);
                menuTeam.setVisibility(View.GONE);
                menuPartner.setVisibility(View.VISIBLE);
                menuExport.setVisibility(View.GONE);
            } else {
                //未登录状态进入首页
                changeFragment(4);
                menuHome.setChecked(true);
                menuHome.setVisibility(View.VISIBLE);
                exportHome.setVisibility(View.GONE);
                exportProduct.setVisibility(View.GONE);
                menuProduct.setVisibility(View.VISIBLE);
                menuMessage.setVisibility(View.VISIBLE);
                exportMessage.setVisibility(View.GONE);
                menuTeam.setVisibility(View.VISIBLE);
                menuPartner.setVisibility(View.GONE);
                menuExport.setVisibility(View.GONE);
            }

        }

        /**
         * 判断当前登录用户是企业还是个人
         * 企业 changeFragment(4);
         */
        layoutNav.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.menu_home:
                        Log.e("chose", chose + "");
                        if (chose == 1) {
                            //2是达人1是企业主3是合伙人
                            changeFragment(4);
                        } else if (chose == 3) {
                            changeFragment(11);
                        } else {
                            //未登录状态进入首页
                            changeFragment(4);
                        }
                        break;
                    case R.id.export_home:
                        //要判断用户是否加入团队
                        changeFragment(5);
                        break;
                    case R.id.export_product:
                        //要判断用户是否加入团队
                        //达人订单
                        changeFragment(12);
                        break;
                    case R.id.menu_product:
                        if (chose == 1) {
                            //2是达人1是企业主3是合伙人
                            //企业订单
                            changeFragment(1);
                        } else if (chose == 3) {
                            //[合伙订单]
                            changeFragment(7);
                        } else {
                            //未登录状态进入首页
                            new AlertDialog.Builder(MainActivity.this).setMessage("请先登录，才有看到订单哦！")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }

                                    }).show();
                        }
                        break;
                    case R.id.export_message:
                        changeFragment(13);
                        break;
                    case R.id.menu_message:
                        //2是达人1是企业主3是合伙人
                        if (chose == 1) {
                            //企业消息
                            changeFragment(2);
                        } else if (chose == 3) {
                            //合伙消息
                            changeFragment(8);
                        } else {
                            //添加未登录没有消息提示
                            new AlertDialog.Builder(MainActivity.this).setMessage("请先登录，才有看到消息哦！")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }

                                    }).show();
                        }
                        break;
                    case R.id.menu_partner:
                        //合伙人
                        changeFragment(3);
                        break;
                    case R.id.menu_export:
                        //先判断达人是否加入团队
                        //[达人]我的团队-加入团队
                        if (StringUtils.isEmpty(team_id))
                            changeFragment(9);
                        else
                            //[达人]我的团队-未加入团队
                            changeFragment(10);
                        break;
                    case R.id.menu_team:
                        //2是达人1是企业主3是合伙人
                        if (chose == 1) {
                            //企业主我的
                            changeFragment(6);

                        } else {
                            //企业主我的, 但是什么点不了，要求登录
                            new AlertDialog.Builder(MainActivity.this).setMessage("请先登录，才能看到我的消息哦！")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }

                                    }).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOrder == true) {
            changeFragment(1);
            exportHome.setChecked(false);
            menuProduct.setChecked(true);
            exportMessage.setChecked(false);
            menuExport.setChecked(false);
            isOrder = false;
        }
    }

    public void changeToCompany() {
        menuHome.setChecked(true);
        changeFragment(4);
        menuHome.setVisibility(View.VISIBLE);
        exportHome.setVisibility(View.GONE);
        exportProduct.setVisibility(View.GONE);
        menuProduct.setVisibility(View.VISIBLE);
        menuMessage.setVisibility(View.VISIBLE);
        exportMessage.setVisibility(View.GONE);
        menuTeam.setVisibility(View.VISIBLE);
        menuPartner.setVisibility(View.GONE);
        menuExport.setVisibility(View.GONE);
    }

    public void changeToPartnerShip() {
        changeFragment(11);
        menuHome.setChecked(true);
        menuHome.setVisibility(View.VISIBLE);
        exportHome.setVisibility(View.GONE);
        exportProduct.setVisibility(View.GONE);
        menuProduct.setVisibility(View.VISIBLE);
        menuMessage.setVisibility(View.VISIBLE);
        exportMessage.setVisibility(View.GONE);
        menuTeam.setVisibility(View.GONE);
        menuPartner.setVisibility(View.VISIBLE);
        menuExport.setVisibility(View.GONE);
    }

    public void changeToExpert() {
        changeFragment(5);
        exportHome.setChecked(true);
        menuHome.setVisibility(View.GONE);
        exportHome.setVisibility(View.VISIBLE);
        exportProduct.setVisibility(View.VISIBLE);
        menuProduct.setVisibility(View.GONE);
        menuMessage.setVisibility(View.GONE);
        exportMessage.setVisibility(View.VISIBLE);
        menuTeam.setVisibility(View.GONE);
        menuPartner.setVisibility(View.GONE);
        menuExport.setVisibility(View.VISIBLE);
    }

    public void setChecked(int i) {
        changeFragment(i);
        menuHome.setChecked(false);
        exportProduct.setChecked(false);
        menuMessage.setChecked(false);
        menuExport.setChecked(true);

    }

    private void hideFragments(FragmentTransaction transaction) {

        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (productFragment != null) {
            transaction.hide(productFragment);
        }
        if (peopleSelfFragment != null) {
            transaction.hide(peopleSelfFragment);
        }
        if (teamFragment != null) {
            transaction.hide(teamFragment);
        }
        if (companyFragment != null) {
            transaction.hide(companyFragment);
        }
        if (peopleHomeFragmet != null) {
            transaction.hide(peopleHomeFragmet);
        }
        if (companyHome != null) {
            transaction.hide(companyHome);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
        if (peopMessageFragment != null) {
            transaction.hide(peopMessageFragment);
        }
        if (peopleTeamFragmet != null) {
            transaction.hide(peopleTeamFragmet);
        }
        if (peopleProductFragment != null) {
            transaction.hide(peopleProductFragment);
        }
        if (peopeleMessageFragment != null) {
            transaction.hide(peopeleMessageFragment);
        }
        if (masterTeamFragment != null) {
            transaction.hide(masterTeamFragment);
        }
    }

    public void changeFragment(int index) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        hideFragments(transaction);
        switch (index) {
            case 1:
                //企业订单
                if (productFragment == null) {
                    productFragment = new ProductFragment();
                    transaction.add(R.id.layout_content, productFragment);
                } else {
                    transaction.show(productFragment);
                }
                break;

            case 2:
                //企业消息
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.layout_content, messageFragment);
                } else {
                    transaction.show(messageFragment);
                }
                break;
            case 3:
                //[合伙]我的团队[成员]
                if (teamFragment == null) {
                    teamFragment = new TeamFragment();
                    transaction.add(R.id.layout_content, teamFragment);
                } else {
                    transaction.show(teamFragment);
                }
                break;
            case 4:
                //企业主页
                if (companyFragment == null) {
                    companyFragment = new CompanyFragment();
                    transaction.add(R.id.layout_content, companyFragment);
                } else {
                    transaction.show(companyFragment);
                }
                break;
            case 5:
                //达人主页
                if (peopleHomeFragmet == null) {
                    peopleHomeFragmet = new PeopleHomeFragmet();
                    transaction.add(R.id.layout_content, peopleHomeFragmet);
                } else {
                    transaction.show(peopleHomeFragmet);
                }
                break;
            case 6:
                //企业--我的
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    transaction.add(R.id.layout_content, mineFragment);
                } else {
                    transaction.show(mineFragment);
                }
                break;
            case 7:
                //[合伙订单]
                if (peopleProductFragment == null) {
                    peopleProductFragment = new PeopleProductFragment();
                    transaction.add(R.id.layout_content, peopleProductFragment);
                } else {
                    transaction.show(peopleProductFragment);
                }
                break;
            case 8:
                //合伙消息
                if (peopeleMessageFragment == null) {
                    peopeleMessageFragment = new PeopeleMessageFragment();
                    transaction.add(R.id.layout_content, peopeleMessageFragment);
                } else {
                    transaction.show(peopeleMessageFragment);
                }
                break;
            case 9:
                //[达人]我的团队-未加入团队
                if (peopleTeamFragmet == null) {
                    peopleTeamFragmet = new PeopleTeamFragmet();
                    transaction.add(R.id.layout_content, peopleTeamFragmet);
                } else {
                    transaction.show(peopleTeamFragmet);
                }
                break;
            case 10:
                //[达人]我的团队-加入团队
                if (masterTeamFragment == null) {
                    masterTeamFragment = new MasterTeamFragment();
                    transaction.add(R.id.layout_content, masterTeamFragment);
                } else {
                    transaction.show(masterTeamFragment);
                }
                break;
            case 11:
                // 合伙-首页
                if (companyHome == null) {
                    companyHome = new CompanyHome();
                    transaction.add(R.id.layout_content, companyHome);
                } else {
                    transaction.show(companyHome);
                }
                break;
            case 12:
                //达人订单
                if (peopleSelfFragment == null) {
                    peopleSelfFragment = new PeopleSelfFragment();
                    transaction.add(R.id.layout_content, peopleSelfFragment);
                } else {
                    transaction.show(peopleSelfFragment);
                }
                break;
            case 13:
                //达人消息
                if (peopMessageFragment == null) {
                    peopMessageFragment = new PeopMessageFragment();
                    transaction.add(R.id.layout_content, peopMessageFragment);
                } else {
                    transaction.show(peopMessageFragment);
                }
                break;
        }

        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

