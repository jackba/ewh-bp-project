/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ewhoxford.android.bloodpressure.ghealth.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.ewhoxford.android.bloodpressure.MeasureListActivity;
import com.ewhoxford.android.bloodpressure.R;

/**
 * Choose which account to upload track information to.
 *
 * @author Sandor Dornbush (originally)
 */
public class AccountChooser {

  /**
   * The last selected account.
   */
  private Account selectedAccount = null;

  /**
   * An interface for receiving updates once the user has selected the account.
   */
  public interface AccountHandler {
    /**
     * Handle the account being selected.
     *
     * @param account
     *          The selected account or null if none could be found
     */
    public void handleAccountSelected(Account account);
  }

  /**
   * Chooses the best account to upload to. If no account is found the user will
   * be alerted. If only one account is found that will be used. If multiple
   * accounts are found the user will be allowed to choose.
   *
   * @param activity
   *          The parent activity
   * @param handler
   *          The handler to be notified when an account has been selected
   */
  public void chooseAccount(final Activity activity, final AccountHandler handler) {
    final Account[] accounts = AccountManager.get(activity).getAccountsByType(
        MeasureListActivity.ACCOUNT_TYPE);

    if (accounts.length < 1) {
      alertNoAccounts(activity, handler);
      return;
    }

    if (accounts.length == 1) {
      handler.handleAccountSelected(accounts[0]);
      return;
    }

    // TODO This should be read out of a preference.
    if (selectedAccount != null) {
      handler.handleAccountSelected(selectedAccount);
      return;
    }

    // Let the user choose.
    Log.i(MeasureListActivity.TAG, "Multiple matching accounts found.");
    final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setTitle(R.string.choose_account_title);
    builder.setCancelable(true);

    String[] choices = new String[accounts.length];
    for (int i = 0; i < accounts.length; i++) {
      choices[i] = accounts[i].name;
    }

    builder.setItems(choices, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        selectedAccount = accounts[which];
        handler.handleAccountSelected(selectedAccount);
      }
    });

    builder.show();
  }

  /**
   * Puts up a dialog alerting the user that no suitable account was found.
   */
  private void alertNoAccounts(final Activity activity, final AccountHandler handler) {
    Log.i(MeasureListActivity.TAG, "No matching accounts found.");
    final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setTitle(R.string.no_account_found_title);
    builder.setMessage(R.string.no_account_found);
    builder.setCancelable(true);
    builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        handler.handleAccountSelected(null);
      }
    });
    builder.show();
  }
}
