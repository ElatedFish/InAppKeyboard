package com.softplus.inappkeyboard;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class CustomKeyboard extends LinearLayout implements View.OnClickListener {
    private Button key1, key2, key3, key4, key5, key6, key7, key8, key9, key0, key00;
    private Button key_delete, key_AC, key_dot;
    private SparseArray<String> keyValues = new SparseArray<>();
    private InputConnection inputConnection;
    private String LogTag = "Keyboard";

    public CustomKeyboard(Context context) {
        super(context);
    }

    public CustomKeyboard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomKeyboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.custom_keyboard, this, true);

        key1 = (Button)findViewById(R.id.key_1);
        key2 = (Button)findViewById(R.id.key_2);
        key3 = (Button)findViewById(R.id.key_3);
        key4 = (Button)findViewById(R.id.key_4);
        key5 = (Button)findViewById(R.id.key_5);
        key6 = (Button)findViewById(R.id.key_6);
        key7 = (Button)findViewById(R.id.key_7);
        key8 = (Button)findViewById(R.id.key_8);
        key9 = (Button)findViewById(R.id.key_9);
        key0 = (Button)findViewById(R.id.key_0);
        key00 = (Button)findViewById(R.id.key_00);
        key_delete = (Button)findViewById(R.id.key_delete);
        key_AC = (Button)findViewById(R.id.key_AC);
        key_dot = (Button)findViewById(R.id.key_dot);

        key1.setOnClickListener(this);
        key2.setOnClickListener(this);
        key3.setOnClickListener(this);
        key4.setOnClickListener(this);
        key5.setOnClickListener(this);
        key6.setOnClickListener(this);
        key7.setOnClickListener(this);
        key8.setOnClickListener(this);
        key9.setOnClickListener(this);
        key0.setOnClickListener(this);
        key00.setOnClickListener(this);
        key_delete.setOnClickListener(this);
        key_AC.setOnClickListener(this);
        key_dot.setOnClickListener(this);

        keyValues.put(R.id.key_1, "1");
        keyValues.put(R.id.key_2, "2");
        keyValues.put(R.id.key_3, "3");
        keyValues.put(R.id.key_4, "4");
        keyValues.put(R.id.key_5, "5");
        keyValues.put(R.id.key_6, "6");
        keyValues.put(R.id.key_7, "7");
        keyValues.put(R.id.key_8, "8");
        keyValues.put(R.id.key_9, "9");
        keyValues.put(R.id.key_0, "0");
        keyValues.put(R.id.key_00, "00");
        keyValues.put(R.id.key_dot, ".");
    }

    @Override
    public void onClick(View view) {
        if (inputConnection == null) {
            Log.i(LogTag, "Input connection == null");
            return;
        }

        CharSequence currentText = inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text;
        CharSequence beforeCursorText = inputConnection.getTextBeforeCursor(currentText.length(), 0);
        CharSequence afterCursorText = inputConnection.getTextAfterCursor(currentText.length(), 0);
        switch (view.getId()) {
            case R.id.key_delete:
                CharSequence selectedText = inputConnection.getSelectedText(0);

                if (TextUtils.isEmpty(selectedText))
                    inputConnection.deleteSurroundingText(1, 0);
                else
                    inputConnection.commitText("", 1);
                break;
            case R.id.key_dot:
                Log.i(LogTag, currentText.toString());
                if (beforeCursorText.length() == 0) // decimal point cannot be first character
                    break;
                if (currentText.toString().indexOf('.') >= 0) // decimal point cannot be repeated
                    break;

                inputConnectionCommitText(view);
                break;
            case R.id.key_AC:
                inputConnection.deleteSurroundingText(beforeCursorText.length(), afterCursorText.length());
                break;
            case R.id.key_0: // Note case order, key_0 and key_00 must before default
            case R.id.key_00:
                if (beforeCursorText.length() == 0)
                    break;
            default:
                inputConnectionCommitText(view);
                break;
        }
    }

    public void setInputConnection(InputConnection ic) {
        inputConnection = ic;
    }

    private void inputConnectionCommitText(View view) {
        String value = keyValues.get(view.getId());
        inputConnection.commitText(value, 1);
    }
}
