# Fix SplashActivity Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Fix `SplashActivity` crash by setting correct layout and using "leaf" logo.

**Architecture:** Update `setContentView` in `SplashActivity.kt` to point to `R.layout.activity_splash`.

**Tech Stack:** Kotlin, Android XML Layout.

---

### Task 1: Update SplashActivity

**Files:**
- Modify: `app/src/main/java/com/makoto/ecopedia/SplashActivity.kt`

- [ ] **Step 1: Fix setContentView**

Replace `setContentView(R.drawable.leaf)` with `setContentView(R.layout.activity_splash)`.

```kotlin
// ... inside onCreate
setContentView(R.layout.activity_splash)
// ...
```

- [ ] **Step 2: Commit changes**

Run: `git add app/src/main/java/com/makoto/ecopedia/SplashActivity.kt; git commit -m "fix: use correct layout for SplashActivity"`

### Task 2: Verification

- [ ] **Step 1: Verify code structure**

Ensure `SplashActivity.kt` no longer references drawable in `setContentView`.

- [ ] **Step 2: Finalize**

Update memory if needed.
