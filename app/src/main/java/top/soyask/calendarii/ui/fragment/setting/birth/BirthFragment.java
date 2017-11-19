package top.soyask.calendarii.ui.fragment.setting.birth;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import top.soyask.calendarii.R;
import top.soyask.calendarii.database.dao.BirthdayDao;
import top.soyask.calendarii.domain.Birthday;
import top.soyask.calendarii.global.GlobalData;
import top.soyask.calendarii.ui.adapter.birth.BirthdayAdapter;
import top.soyask.calendarii.ui.fragment.base.BaseFragment;
import top.soyask.calendarii.ui.widget.WidgetManager;

public class BirthFragment extends BaseFragment implements View.OnClickListener, BirthdayAdapter.OnBirthdayClickListener, AddFragment.OnDoneClickListener {

    private BirthdayDao mBirthdayDao;
    private BirthdayAdapter mBirthdayAdapter;
    private List<Birthday> mBirthdays;

    public BirthFragment() {
        super(R.layout.fragment_birth);
    }

    public static BirthFragment newInstance() {
        BirthFragment fragment = new BirthFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void setupUI() {
        mBirthdayDao = BirthdayDao.getInstance(getMainActivity());
        findToolbar().setNavigationOnClickListener(this);
        setupRecycleView();
    }

    private void setupRecycleView() {
        mBirthdays = mBirthdayDao.queryAll();
        mBirthdayAdapter = new BirthdayAdapter(mBirthdays, this);

        RecyclerView rv = findViewById(R.id.rv_birth);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(mBirthdayAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                removeFragment(this);
                break;
        }
    }

    @Override
    public void onBirthdayClick() {
    }

    @Override
    public void onBirthdayLongClick(final int position, final Birthday birthday) {
        new AlertDialog
                .Builder(getMainActivity())
                .setItems(new String[]{"删除"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = birthday.getId();
                        mBirthdayDao.delete(id);
                        mBirthdays.remove(birthday);
                        mBirthdayAdapter.notifyItemRemoved(position);
                        mBirthdayAdapter.notifyItemRangeChanged(0, position);
                        GlobalData.loadBirthday(getMainActivity());
                        WidgetManager.updateAllWidget(getMainActivity());
                    }
                }).show();
    }

    @Override
    public void addBirthday() {
        AddFragment addFragment = AddFragment.newInstance();
        addFragment(addFragment);
        addFragment.setOnDoneClickListener(this);
    }

    @Override
    public void onDone(Birthday birthday) {
        mBirthdays.clear();
        mBirthdays.addAll(mBirthdayDao.queryAll());
        mBirthdayAdapter.notifyDataSetChanged();
        GlobalData.loadBirthday(getMainActivity());
        WidgetManager.updateAllWidget(getMainActivity());
    }
}