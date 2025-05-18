(function (rawActivity) {
    function formatTime(e) {
        if (e === 0)
            return "0 mins";
        const u = e / (1e3 * 60);
        if (u < .1)
            return `${u.toFixed(3)} mins`;
        const l = Math.floor(u / 60)
            , r = Math.floor(l / 24)
            , o = l % 24
            , i = u % 60;
        let f = [];
        if (r > 0 && f.push(`${r} day${r === 1 ? "" : "s"}`),
            o > 0 && f.push(`${o} hour${o === 1 ? "" : "s"}`),
            i > 0) {
            const s = i.toFixed(1);
            f.push(`${s} min${s === "1.0" ? "" : "s"}`)
        }
        return f.join(" ")
    }

    function L(e) {return e();}

    function $t() {return {heatmapActivityByUsername: (_) => JSON.parse(rawActivity)}; }

    const tools = $t()
        , profile = {username: "commie"}
        , r = () => {
            const curDate = new Date
                , timeZoneOffset = -curDate.getTimezoneOffset()
                , dayBeginMs = new Date(curDate.getFullYear(), curDate.getMonth(), curDate.getDate())
                , dayBegin = Math.floor(dayBeginMs.getTime() / 1e3)
                , lastDayBegin = Math.floor(curDate.getTime() / 1e3) - 24 * 60 * 60
                , lastMonthBegin = new Date(curDate);
            lastMonthBegin.setDate(curDate.getDate() - 30),
                lastMonthBegin.setHours(0, 0, 0, 0);
            const _ = Math.floor(lastMonthBegin.getTime() / 1e3) - timeZoneOffset * 60;
            return {
                startOfToday: dayBegin,
                last24Hours: lastDayBegin,
                last30Days: _
            }
        }
        , todayActivity = L(() => {
            if (!profile.username)
                return "0 minutes";
            const { startOfToday: v } = r()
                , c = tools.heatmapActivityByUsername(profile.username);
            let b = 0;
            return c.forEach(S => {
                S[0] >= v && (b += S[2])
            }
            ),
                b === 0 ? "0 minutes" : formatTime(b)
        }
        )
        , o = L(() => {
            if (!profile.username)
                return "0 minutes";
            const { last24Hours: v } = r()
                , c = tools.heatmapActivityByUsername(profile.username);
            let b = 0;
            return c.forEach(S => {
                S[0] >= v && (b += S[2])
            }
            ),
                b === 0 ? "0 minutes" : formatTime(b)
        }
        )
        , l = L(() => {
            if (!profile.username)
                return "0 minutes";
            const { last30Days: v } = r()
                , c = tools.heatmapActivityByUsername(profile.username);
            let b = 0;
            return c.forEach(S => {
                S[0] >= v && (b += S[2])
            }
            ),
                b === 0 ? "0 minutes" : formatTime(b)
        }
        )
        , s = L(() => {
            if (!profile.username)
                return "0 minutes";
            const v = new Date
                , c = new Date(v.getFullYear(), 0, 1)
                , b = -c.getTimezoneOffset()
                , S = Math.floor(c.getTime() / 1e3) - b * 60
                , h = tools.heatmapActivityByUsername(profile.username);
            let f = 0;
            return h.forEach(_ => {
                _[0] >= S && (f += _[2])
            }
            ),
                f === 0 ? "0 minutes" : formatTime(f)
        }
        )
        , u = L(() => {
            if (!profile.username)
                return "0 minutes";
            const v = tools.heatmapActivityByUsername(profile.username);
            let c = 0;
            return v.forEach(b => {
                c += b[2]
            }
            ),
                c === 0 ? "0 minutes" : formatTime(c)
        }
        )
        , y = L(() => {
            let v = new Map;
            const b = -new Date().getTimezoneOffset();
            return tools.heatmapActivityByUsername(profile.username).forEach(h => {
                const _ = new Date((h[0] + b * 60) * 1e3).toISOString().split("T")[0]
                    , g = v.get(_) || 0;
                v.set(_, g + h[2])
            }
            ),
                Array.from(v, ([h, f]) => ({
                    date: Date.parse(h + "T00:00:00"),
                    count: (f / 1e3 / 60).toFixed(2)
                }))
        }
        )
        , streaks = L(() => {
            const v = y;
            if (!v || v.length === 0)
                return {
                    currentStreak: 0,
                    allTimeStreak: 0
                };
            const c = [...v].sort((p, w) => p.date - w.date)
                , b = (p, w) => Math.round((w - p) / (24 * 60 * 60 * 1e3));
            let S = 1
                , h = 1
                , f = 1;
            for (let p = 1; p < c.length; p++)
                b(c[p - 1].date, c[p].date) === 1 ? f++ : (f > h && (h = f),
                    f = 1);
            f > h && (h = f),
                S = h;
            let _ = 1;
            for (let p = c.length - 1; p > 0; p--) {
                const w = p - 1;
                if (b(c[w].date, c[p].date) === 1)
                    _++;
                else
                    break
            }
            const g = new Date;
            g.setHours(0, 0, 0, 0);
            const E = new Date(g);
            E.setDate(g.getDate() - 1);
            const a = new Date(c[c.length - 1].date);
            return a.setHours(0, 0, 0, 0),
                a.getTime() !== g.getTime() && a.getTime() !== E.getTime() && (_ = 0),
            {
                currentStreak: _,
                allTimeStreak: S
            }
        }
        );

    return { dawTime: todayActivity, streak: streaks.currentStreak };
})