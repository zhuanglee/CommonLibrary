package cn.lzh.ui.helper;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


/**
 * 加载工作模块的菜单项
 *
 * @author lzh
 * @see ISmartMenuItem
 * @see SmartMenuHelper.SmartMenuItem
 */
public final class SmartMenuHelper {

    private SmartMenuHelper() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 加载菜单<br/>
     *
     * @param activity Activity
     * @param menuResId 菜单资源id
     * @param menu Menu
     * @param filterItemIds 过滤的菜单id列表
     */
    public static List<ISmartMenuItem> inflateMenu(
            Activity activity, int menuResId, Menu menu, int... filterItemIds) {
        activity.getMenuInflater().inflate(menuResId, menu);
        return convert(menu, filterItemIds);
    }

    /**
     * 加载菜单<br/>
     *
     * @param inflater MenuInflater
     * @param menuResId 菜单资源id
     * @param menu Menu
     * @param filterItemIds 过滤的菜单id列表
     */
    public static List<ISmartMenuItem> inflateMenu(
            MenuInflater inflater, int menuResId, Menu menu, int... filterItemIds) {
        inflater.inflate(menuResId, menu);
        return convert(menu, filterItemIds);
    }

    /**
     * 将{@link Menu}转化为自定义菜单列表，并执行{@link Menu#clear()}
     *
     * @param menu Menu
     * @param filterItemIds 过滤的菜单id列表
     */
    private static List<ISmartMenuItem> convert(Menu menu, int... filterItemIds) {
        SparseBooleanArray map = new SparseBooleanArray();
        for (int itemId : filterItemIds) {
            map.put(itemId, true);
        }
        List<ISmartMenuItem> items = new ArrayList<>();
        int size = menu.size();
        ISmartMenuItem workMenu;
        MenuItem item;
        for (int i = 0; i < size; i++) {
            item = menu.getItem(i);
            if (item.isVisible() && !map.get(item.getItemId())) {
                workMenu = new SmartMenuItem(item);
                items.add(workMenu);
            }
        }
        menu.clear();
        return items;
    }

    /**
     * 过滤指定菜单项
     *
     * @param items 菜单项列表
     * @param itemId 菜单项的Id
     */
    public static void filterById(List<ISmartMenuItem> items, int itemId) {
        ISmartMenuItem filterMenuItem = null;
        for (ISmartMenuItem item : items) {
            if (item.getItemId() == itemId) {
                filterMenuItem = item;
                break;
            }
        }
        if (filterMenuItem != null) {
            items.remove(filterMenuItem);
        }
    }

    /**
     * 工作菜单项
     * @author lzh
     *
     */
    private static class SmartMenuItem implements ISmartMenuItem {

        private final int itemId;
        private int groupId;
        private CharSequence title;
        private Drawable icon;
        private boolean checked;
        private boolean visible;
        private int order;
        private boolean enabled;
        /**未读消息数*/
        private int unreadCount;

        /**
         * 根据MenuItem构造工作菜单项
         * @param menuItem MenuItem
         */
        public SmartMenuItem(MenuItem menuItem) {
            this.itemId = menuItem.getItemId();
            this.groupId = menuItem.getGroupId();
            this.title = menuItem.getTitle();
            this.icon = menuItem.getIcon();
            this.visible = menuItem.isVisible();
            this.enabled = menuItem.isEnabled();
            this.order = menuItem.getOrder();
        }

        @Override
        public int getItemId() {
            return itemId;
        }

        @Override
        public int getGroupId() {
            return groupId;
        }

        @Override
        public ISmartMenuItem setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        @Override
        public CharSequence getTitle() {
            return title;
        }

        @Override
        public ISmartMenuItem setIcon(Drawable icon) {
            this.icon = icon;
            return this;
        }

        @Override
        public Drawable getIcon() {
            return icon;
        }

        @Override
        public ISmartMenuItem setChecked(boolean checked) {
            this.checked = checked;
            return this;
        }

        @Override
        public boolean isChecked() {
            return checked;
        }

        @Override
        public ISmartMenuItem setVisible(boolean visible) {
            this.visible = visible;
            return this;
        }

        @Override
        public boolean isVisible() {
            return visible;
        }

        @Override
        public ISmartMenuItem setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public ISmartMenuItem setOrder(int order) {
            this.order = order;
            return this;
        }

        @Override
        public int getOrder() {
            return order;
        }

        @Override
        public ISmartMenuItem setUnreadCount(int unreadCount) {
            this.unreadCount = unreadCount;
            return this;
        }

        @Override
        public int getUnreadCount() {
            return unreadCount;
        }
    }

}
