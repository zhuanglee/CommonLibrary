package cn.lzh.ui.helper;

import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;

/**
 * Created by lzh on 2017/5/24 09:54.<br/>
 * 工作菜单项接口，参考{@link MenuItem}
 */
public interface ISmartMenuItem extends Serializable{

	/**
	 * Return the identifier for this menu item.  The identifier can not
	 * be changed after the menu is created.
	 *
	 * @return The menu item's identifier.
	 */
	int getItemId();

	/**
	 * Return the getStepGroup identifier that this menu item is part of. The getStepGroup
	 * identifier can not be changed after the menu is created.
	 *
	 * @return The menu item's getStepGroup identifier.
	 */
	int getGroupId();

	/**
	 * Change the title associated with this item.
	 *
	 * @param title The new text to be displayed.
	 * @return This Item so additional setters can be called.
	 */
	ISmartMenuItem setTitle(CharSequence title);

	/**
	 * Return the current title of the item.
	 *
	 * @return The title.
	 */
	CharSequence getTitle();

	/**
	 * Change the icon associated with this item.
	 *
	 * @param icon The new icon (as a Drawable) to be displayed.
	 * @return This Item so additional setters can be called.
	 */
	ISmartMenuItem setIcon(Drawable icon);

	/**
	 * Returns the icon for this item as a Drawable (getting it from resources if it hasn't been
	 * loaded before).
	 *
	 * @return The icon as a Drawable.
	 */
	Drawable getIcon();

	/**
	 * Control whether this item is shown with a check mark.
	 * <p>
	 * See {@link Menu} for the menu types that support check marks.
	 *
	 * @see #isChecked
	 * @param checked Set to true to display a check mark, false to hide
	 *                it.  The default value is false.
	 * @return This Item so additional setters can be called.
	 */
	ISmartMenuItem setChecked(boolean checked);

	/**
	 * Return whether the item is currently displaying a check mark.
	 *
	 * @return If a check mark is displayed, returns true.
	 *
	 * @see #setChecked
	 */
	boolean isChecked();

	/**
	 * Sets the visibility of the menu item.
	 *
	 * @param visible If true then the item will be visible; if false it is
	 *        hidden.
	 * @return This Item so additional setters can be called.
	 */
	ISmartMenuItem setVisible(boolean visible);

	/**
	 * Return the visibility of the menu item.
	 *
	 * @return If true the item is visible; else it is hidden.
	 */
	boolean isVisible();

	/**
	 * Sets whether the menu item is enabled.
	 *
	 * @param enabled If true then the item will be invokable; if false it is
	 *        won't be invokable.
	 * @return This Item so additional setters can be called.
	 */
	ISmartMenuItem setEnabled(boolean enabled);

	/**
	 * Return the enabled state of the menu item.
	 *
	 * @return If true the item is enabled and hence invokable; else it is not.
	 */
	boolean isEnabled();

	/**
	 * Set the order of the menu item.
	 * @param order The new order to be displayed.
	 * @return This Item so additional setters can be called.
	 */
	ISmartMenuItem setOrder(int order);

	/**
	 * Return the category and order within the category of this item. This
	 * item will be shown before all items (within its category) that have
	 * order greater than this value.
	 * @return The order of this item.
	 */
	int getOrder();

	/**
	 * Set the unread message count of the menu item.
	 * @param unreadCount The new unread message count to be displayed.
	 * @return This Item so additional setters can be called.
	 */
	ISmartMenuItem setUnreadCount(int unreadCount);

	/**
	 * Return the unread message count of the menu item.
	 */
	int getUnreadCount();

}
