package cn.lzh.ui.helper;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.lang.reflect.Constructor;
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
    /**
     * Android内部隐藏的类，{@link Menu}接口的实现类
     */
    private static final String COM_ANDROID_INTERNAL_VIEW_MENU_MENU_BUILDER = "com.android.internal.view.menu.MenuBuilder";

    private SmartMenuHelper() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 模拟点击菜单按键，用于触发加载菜单(异步操作)<br/>
     *
     * @Deprecated 偶尔会报java.lang.SecurityException:<br/>
     * Injecting to another application requires INJECT_EVENTS permission<br/>
     */
    @Deprecated
    public static void clickMenu() {
        try {
            Instrumentation inst = new Instrumentation();
            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * 预加载菜单项<br/>
     *
     * @param context       上下文
     * @param menuResId     菜单资源id
     * @param filterItemIds 过滤的菜单id列表
     */
    public static synchronized List<ISmartMenuItem> preLoadingMenu(Context context, int menuResId, int... filterItemIds) {
        Menu menu = inflateMenu(context, menuResId);
        return convert(menu, filterItemIds);
    }

    /**
     * 加载菜单<br/>
     *
     * @param context Activity
     * @param menuResId 菜单资源id
     * @param menu Menu
     * @param filterItemIds 过滤的菜单id列表
     */
    public static synchronized List<ISmartMenuItem> inflateMenu(Activity context, int menuResId, Menu menu, int... filterItemIds) {
        context.getMenuInflater().inflate(menuResId, menu);
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
    public static synchronized List<ISmartMenuItem> inflateMenu(MenuInflater inflater, int menuResId, Menu menu, int... filterItemIds) {
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
        List<Integer> filter = new ArrayList<>();
        if (filterItemIds != null && filterItemIds.length > 0) {
            for(int i = filterItemIds.length - 1; i >= 0; i--){
                filter.add(filterItemIds[i]);
            }
        }
        List<ISmartMenuItem> items = new ArrayList<>();
        int size = menu.size();
        ISmartMenuItem workMenu;
        MenuItem item;
        for (int i = 0; i < size; i++) {
            item = menu.getItem(i);
            if (item.isVisible() && !filter.contains(item.getItemId())) {
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
    public static synchronized void filterById(List<ISmartMenuItem> items, int itemId) {
        ISmartMenuItem filterMenuItem = null;
        for (ISmartMenuItem item : items) {
            if (item.getItemId() == itemId) {
                filterMenuItem = item;
            }
        }
        if (filterMenuItem != null) {
            items.remove(filterMenuItem);
        }
    }

    /**
     * 加载菜单，可随时调用，之前只能在用户点击（或代码模拟点击{@link Instrumentation#sendKeyDownUpSync(int)}）菜单按键时才能初始化菜单；<br/>
     * 1、通过反射创建 {@link Menu}实例；<br/>
     * 2、仍然使用{@link MenuInflater}加载菜单；<br/>
     *
     * @param ctx Context
     * @param menuRes 菜单文件资源ID
     * @return Menu
     */
    private static Menu inflateMenu(Context ctx, int menuRes) {
        Menu menu = null;
        try {
            menu = createMenuBuilder(ctx);
            MenuInflater menuInflater = new MenuInflater(ctx);
            menuInflater.inflate(menuRes, menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menu;
    }

    /**
     * 通过反射创建 {@link Menu}实例（com.android.internal.view.menu.MenuBuilder）
     *
     * @param ctx Context
     */
    private static Menu createMenuBuilder(Context ctx)
            throws Exception {
        Class<?> menuBuilderClass = Class.forName(COM_ANDROID_INTERNAL_VIEW_MENU_MENU_BUILDER);
        Class[] paramTypes = {Context.class};
        Constructor con = menuBuilderClass.getConstructor(paramTypes);
        return (Menu) con.newInstance(ctx);
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
         * @param menuItem
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
