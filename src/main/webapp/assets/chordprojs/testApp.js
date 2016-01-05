#!/usr/bin/env node
/*
 * Copyright (c) 2011 Jonathan Perkin <jonathan@perkin.org.uk>
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

/*
 * Project page:  http://github.com/jperkin/chordpro.js/
 * Personal page: http://www.perkin.org.uk/
 */

var chordpro = require("./dist/chordpro.js");
var fs = require("fs");
var file = fs.readFileSync("song.txt");
var parseResult = chordpro.parse(file.toString());
var formatResult = chordpro.formatParseResult(parseResult);
console.log(formatResult.html);

var begin = '<!DOCTYPE html><html lang="en"><head>	<meta charset="UTF-8">	<title>Document</title>	<link rel="stylesheet" href="lib/chordpro.css"></head><body>	<div class="song">';

var end = '	</div></body></html>';

fs.writeFile("song.html", begin + formatResult.html + end);