Gitflow: A Git Branching Strategy
For a project of this complexity with multiple developers (even if it's just you acting as multiple roles), a structured branching model is essential. Gitflow is a popular and robust strategy.

Key Branches
main (or master)

This branch contains production-ready code.

It should always be stable and deployable.

Direct commits to main are forbidden. Merges only happen from the release branches.

Each commit on main is a new production release and should be tagged with a version number (e.g., v1.0.0).

develop

This is the main development branch where all completed features are integrated.

It's the source for nightly builds or continuous integration.

This branch should also be kept stable, but it represents the "next release" rather than the "current release."

Supporting Branches
feature/* (e.g., feature/user-login)

Branched off from develop.

Used to develop new features. The work for user-service and driver-service would happen in separate feature branches.

When the feature is complete, it is merged back into develop.

These branches should never interact directly with main.

release/* (e.g., release/v1.1.0)

Branched off from develop when you're ready to prepare a new production release.

This branch is for final bug fixes, documentation generation, and other release-oriented tasks. No new features are added here.

Once it's ready, the release branch is merged into both main (and tagged) and develop (to ensure any bug fixes in the release branch are also in the next development cycle).

hotfix/* (e.g., hotfix/login-bug-v1.0.1)

Branched off from main.

Used for critical bugs in a production version that must be fixed immediately.

Once the fix is complete, it's merged into both main (and tagged with a new patch version) and develop.
