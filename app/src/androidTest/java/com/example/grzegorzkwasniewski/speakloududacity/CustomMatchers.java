package com.example.grzegorzkwasniewski.speakloududacity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


public class CustomMatchers {

    public static Matcher<View> withDrawable(int resourceId) {
        return new DrawableMatcher(resourceId);
    }

    public static Matcher<View> noDrawable() {
        return new DrawableMatcher(-1);
    }

    public static Matcher<View> hasDrawable() {
        return new DrawableMatcher(DrawableMatcher.ANY);
    }
}

class DrawableMatcher extends TypeSafeMatcher<View> {

    //region Class Fields

    private final int expectedId;
    private String resourceName;
    static final int EMPTY = -1;
    static final int ANY = -2;

    //endregion

    //region Class Constructs

    public DrawableMatcher(int resourceId) {
        super(View.class);
        this.expectedId = resourceId;
    }

    //endregion

    //region Private Methods

    @Override
    protected boolean matchesSafely(View target) {

        // check if view is of the type ImageView
        if (!(target instanceof ImageView)) {
            return false;
        }

        // cast view to ImageView
        ImageView imageView = (ImageView) target;

        // use with method noDrawable()
        if (expectedId == EMPTY) {
            return imageView.getDrawable() == null;
        }

        if (expectedId == ANY) {
            return imageView.getDrawable() != null;
        }

        // get drawable with given id
        // access context of the target view
        Resources resources = target.getContext().getResources();
        Drawable expectedDrawable = resources.getDrawable(expectedId);

        // check if drawable was found
        if (expectedDrawable == null) {
            return false;
        }

        // check if current view's drawable id equal to drawable with given id
        Bitmap bitmap = getBitmap(imageView.getDrawable());
        Bitmap otherBitmap = getBitmap(expectedDrawable);

        return bitmap.sameAs(otherBitmap);
    }

    /**
     * Used to create bitmap from drawable
     * Support vectors
     *
     * @param drawable Drawable that will be used for creating bitmap
     * @return Created bitmap
     */

    private Bitmap getBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    //endregion

    //region Public Methods

    @Override
    public void describeTo(Description description) {
        description.appendText("with drawable from resource id: ");
        description.appendValue(expectedId);
        if (resourceName != null) {
            description.appendText("[");
            description.appendText(resourceName);
            description.appendText("]");
        }

        //endregion
    }
}
