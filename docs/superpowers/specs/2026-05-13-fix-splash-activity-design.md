# Design Spec: Fix SplashActivity

## Goal
Fix `SplashActivity` crash/incorrect behavior by setting the correct content view and ensuring the "leaf" logo is used.

## Research
- `SplashActivity.kt` uses `setContentView(R.drawable.leaf)`, which is incorrect for an Activity.
- `activity_splash.xml` already contains an `ImageView` with `android:src="@drawable/leaf"`.
- `leaf.xml` is a valid vector drawable.

## Approach
1. Modify `SplashActivity.kt` to call `setContentView(R.layout.activity_splash)`.
2. Verify `activity_splash.xml` uses `@drawable/leaf` (already confirmed).

## Testing
1. Verify `setContentView` points to `R.layout.activity_splash`.
2. Ensure no build errors.
