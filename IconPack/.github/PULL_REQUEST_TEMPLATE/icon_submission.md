# Icon submission

Add every source SVG to `submissions/`, then complete one object below for each
SVG. The `file` value must exactly match its repository path. Duplicate the
object, separated by a comma, when submitting multiple icons.

Do not edit `svg/` or `metadata.json` yourself: those are generated from this
submission when the pull request is merged, and a change to them here fails the
check. Only the files under `submissions/` belong to a submission.

<!-- ICON_METADATA_START -->
```json
{
  "icons": [
    {
      "name": "Icon display name",
      "author": "Author display name",
      "file": "submissions/example.svg",
      "link": "https://github.com/your-username/IconPack"
    }
  ]
}
```
<!-- ICON_METADATA_END -->

## Submission checks

- [ ] Every submitted file is an SVG, no larger than 700KB.
- [ ] The only files I changed are the ones under `submissions/`.
- [ ] Every SVG is original work or is licensed for redistribution.
- [ ] The JSON above is valid and contains one object per submitted SVG.
- [ ] I grant AmaBeat permission to distribute these icons as part of the icon pack.
