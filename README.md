# captain-sonar-radar-companion

## TODO
- custom map option
- option to switch path view off
- switch between possible current position & possible start positions
- torpedo mode doesn't always switch back to path mode properly

## Bisect Results
Performed Git Bisect to find where map touch coordinates began getting offset:

a7ea5400e6945f0191564b932c30e0a38ccdd256 is the first bad commit
commit a7ea5400e6945f0191564b932c30e0a38ccdd256
Author: Matthew Duffin <matthewtduffin@gmail.com>
Date:   Fri Oct 21 12:31:48 2016 -0700

    added toolbar back in

    :040000 040000 fcd2561454c8fc97ebf51a9a360c51834ce5f627 7cc45ee6942a40bc60a4803d5649276fedf5ed37 M  app
