# Upgrading to Spring Framework 6.x

<div style="
    border: 1px solid rgb(222, 222, 222);
    box-shadow: rgba(0, 0, 0, 0.06) 0px 1px 3px;
  ">
<style>
.w {
	overflow: hidden;
	margin: 0;
	padding: 0;
	background: none transparent;
	text-align: left;
}
.em > a,
.tc > a,
.th > a {
	background-color: transparent;
	-webkit-text-decoration-skip: objects;
}
.em a:not([href]):not([tabindex]),
.tc a:not([href]):not([tabindex]),
.th a:not([href]):not([tabindex]) {
	color: inherit;
	text-decoration: none;
}
.em a:not([href]):not([tabindex]):focus,
.tc a:not([href]):not([tabindex]):focus,
.th a:not([href]):not([tabindex]):focus {
	outline: 0;
}
.em > a,
.tc > a,
.th > a {
	text-decoration: none;
	color: inherit;
	-ms-touch-action: manipulation;
	touch-action: manipulation;
}
.w {
	line-height: 1.4;
	font-family: -apple-system, system-ui, BlinkMacSystemFont, 'Segoe UI',
		Roboto, 'Helvetica Neue', Arial, sans-serif;
	font-weight: 400;
	font-size: 15px;
	color: inherit;
	-webkit-hyphens: auto;
	-moz-hyphens: auto;
	-ms-hyphens: auto;
	hyphens: auto;
	word-wrap: break-word;
	overflow-wrap: break-word;
}
._rtl {
	direction: rtl;
	text-align: right;
}
.t,
.w,
.wf {
	display: -ms-flexbox;
	display: flex;
	-ms-flex-direction: column;
	flex-direction: column;
	max-width: 100%;
	width: 100%;
}
@supports (-webkit-overflow-scrolling: touch) {
	.w {
		max-width: 100vw;
	}
}
.wc,
.wt {
	overflow: hidden;
}
._sc,
._sm {
	background: inherit;
}
._or .tf {
	-ms-flex-order: 0;
	order: 0;
}
._or .th {
	-ms-flex-order: 1;
	order: 1;
}
._or .td {
	-ms-flex-order: 2;
	order: 2;
}
._alsr._ls .wf {
	-ms-flex-direction: column-reverse;
	flex-direction: column-reverse;
}
._alcr._lc .wf {
	-ms-flex-direction: row-reverse;
	flex-direction: row-reverse;
}
._sc._ls .wt,
._ss._ls .wt {
	padding-left: 0;
	padding-right: 0;
}
._sc._ls._alsd .wt,
._ss._ls._alsd .wt {
	padding-bottom: 0;
}
._sc._ls._alsr .wt,
._ss._ls._alsr .wt {
	padding-top: 0;
}
._sc._lc .wt,
._ss._lc .wt {
	padding-top: 0;
	padding-bottom: 0;
}
._ss._lc._alcd .wt {
	padding-right: 0;
}
._ss._lc._alcr .wt {
	padding-left: 0;
}
._lc .wf {
	-ms-flex-direction: row;
	flex-direction: row;
}
._lc .wt {
	display: -ms-flexbox;
	display: flex;
	-ms-flex: 1;
	flex: 1;
	-ms-flex-align: center;
	align-items: center;
}
._sc._lc._alcd .wf {
	padding-right: 0 !important;
}
._sc._lc._alcr .wf {
	padding-left: 0 !important;
}
.wt {
	padding: 8px 10px;
}
@media (min-width: 360px) {
	.wt {
		padding: 12px 15px;
	}
}
@media (min-width: 600px) {
	.wt {
		padding: 16px 20px;
	}
}
._lc._sm:not(.xd) .wc {
	min-width: 100px;
	width: 100px;
	min-height: 100px;
}
@media (min-width: 360px) {
	._lc._sm:not(.xd) .wc {
		min-width: 110px;
		width: 110px;
		min-height: 110px;
	}
}
@media (min-width: 460px) {
	._lc._sm:not(.xd) .wc {
		min-width: 140px;
		width: 140px;
		min-height: 140px;
	}
}
@media (min-width: 600px) {
	._lc._sm:not(.xd) .wc {
		min-width: 160px;
		width: 160px;
		min-height: 160px;
	}
}
._lc._sm._xd:not(._xf) .wc,
._lc._sm._xf:not(._xd) .wc {
	min-width: 100px;
	width: 100px;
	min-height: 100px;
}
@media (min-width: 360px) {
	._lc._sm._xd:not(._xf) .wc,
	._lc._sm._xf:not(._xd) .wc {
		min-width: 110px;
		width: 110px;
		min-height: 110px;
	}
}
@media (min-width: 600px) {
	._lc._sm._xd:not(._xf) .wc,
	._lc._sm._xf:not(._xd) .wc {
		min-width: 120px;
		width: 120px;
		min-height: 120px;
	}
}
._lc._sm._xd._xf .wc {
	min-width: 100px;
	width: 100px;
	min-height: 100px;
}
._lc._sc:not(.xd) .wc {
	min-width: 92px;
	width: 92px;
	min-height: 92px;
}
@media (min-width: 360px) {
	._lc._sc:not(.xd) .wc {
		min-width: 100px;
		width: 100px;
		min-height: 100px;
	}
}
@media (min-width: 460px) {
	._lc._sc:not(.xd) .wc {
		min-width: 130px;
		width: 130px;
		min-height: 130px;
	}
}
@media (min-width: 600px) {
	._lc._sc:not(.xd) .wc {
		min-width: 145px;
		width: 145px;
		min-height: 145px;
	}
}
._lc._sc._xd:not(._xf) .wc,
._lc._sc._xf:not(._xd) .wc {
	min-width: 92px;
	width: 92px;
	min-height: 92px;
}
@media (min-width: 360px) {
	._lc._sc._xd:not(._xf) .wc,
	._lc._sc._xf:not(._xd) .wc {
		min-width: 100px;
		width: 100px;
		min-height: 100px;
	}
}
@media (min-width: 600px) {
	._lc._sc._xd:not(._xf) .wc,
	._lc._sc._xf:not(._xd) .wc {
		min-width: 110px;
		width: 110px;
		min-height: 110px;
	}
}
._lc._sc._xd._xf .wc {
	min-width: 92px;
	width: 92px;
	min-height: 92px;
}
@supports (-moz-appearance: meterbar) and (all: initial) {
	._lc .wc {
		display: -ms-flexbox;
		display: flex;
		-ms-flex-direction: column;
		flex-direction: column;
		-ms-flex-align: stretch;
		align-items: stretch;
		-ms-flex-line-pack: stretch;
		align-content: stretch;
	}
}
._lc._ss:not(.xd) .wc {
	min-width: 100px;
	width: 100px;
	min-height: 100px;
}
@media (min-width: 360px) {
	._lc._ss:not(.xd) .wc {
		min-width: 110px;
		width: 110px;
		min-height: 110px;
	}
}
@media (min-width: 460px) {
	._lc._ss:not(.xd) .wc {
		min-width: 140px;
		width: 140px;
		min-height: 140px;
	}
}
@media (min-width: 600px) {
	._lc._ss:not(.xd) .wc {
		min-width: 160px;
		width: 160px;
		min-height: 160px;
	}
}
._lc._ss._xd:not(._xf) .wc,
._lc._ss._xf:not(._xd) .wc {
	min-width: 100px;
	width: 100px;
	min-height: 100px;
}
@media (min-width: 360px) {
	._lc._ss._xd:not(._xf) .wc,
	._lc._ss._xf:not(._xd) .wc {
		min-width: 110px;
		width: 110px;
		min-height: 110px;
	}
}
@media (min-width: 600px) {
	._lc._ss._xd:not(._xf) .wc,
	._lc._ss._xf:not(._xd) .wc {
		min-width: 120px;
		width: 120px;
		min-height: 120px;
	}
}
._lc._ss._xd._xf .wc {
	min-width: 100px;
	width: 100px;
	min-height: 100px;
}
._sc .wf {
	padding: 8px;
}
@media (min-width: 360px) {
	._sc:not(._xd):not(._xf) .wf {
		padding: 10px;
	}
}
@media (min-width: 460px) {
	._sc:not(._xd):not(._xf) .wf {
		padding: 12px;
	}
}
@media (min-width: 600px) {
	._sc:not(._xd):not(._xf) .wf {
		padding: 16px;
	}
}
._ls .th {
	-webkit-line-clamp: 2;
}
._ls._lh10 .th {
	max-height: 2em;
}
._ls._lh11 .th {
	max-height: 2.2em;
}
._ls._lh12 .th {
	max-height: 2.4em;
}
._ls._lh13 .th {
	max-height: 2.6em;
}
._ls._lh14 .th {
	max-height: 2.8em;
}
._ls._lh15 .th {
	max-height: 3em;
}
._ls .td {
	-webkit-line-clamp: 3;
}
._ls._lh10 .td {
	max-height: 3em;
}
._ls._lh11 .td {
	max-height: 3.3em;
}
._ls._lh12 .td {
	max-height: 3.6em;
}
._ls._lh13 .td {
	max-height: 3.9em;
}
._ls._lh14 .td {
	max-height: 4.2em;
}
._ls._lh15 .td {
	max-height: 4.5em;
}
._ls .twd {
	display: none;
}
@media (max-width: 459px) {
	._lc .ti,
	._lc .tm,
	._lc .tw + .tx,
	._lc .twt {
		display: none;
	}
}
@media (min-width: 460px) {
	._lc .twd {
		display: none;
	}
}
._lc:not(._ap):not(._ts) .th {
	-webkit-line-clamp: 3;
}
._lc:not(._ap):not(._ts)._lh10 .th {
	max-height: 3em;
}
._lc:not(._ap):not(._ts)._lh11 .th {
	max-height: 3.3em;
}
._lc:not(._ap):not(._ts)._lh12 .th {
	max-height: 3.6em;
}
._lc:not(._ap):not(._ts)._lh13 .th {
	max-height: 3.9em;
}
._lc:not(._ap):not(._ts)._lh14 .th {
	max-height: 4.2em;
}
._lc:not(._ap):not(._ts)._lh15 .th {
	max-height: 4.5em;
}
@media (max-width: 359px) {
	._lc:not(._ap):not(._ts) .td {
		display: none;
	}
}
@media (min-width: 360px) {
	._lc:not(._ap):not(._ts) .th {
		-webkit-line-clamp: 2;
	}
	._lc:not(._ap):not(._ts)._lh10 .th {
		max-height: 2em;
	}
	._lc:not(._ap):not(._ts)._lh11 .th {
		max-height: 2.2em;
	}
	._lc:not(._ap):not(._ts)._lh12 .th {
		max-height: 2.4em;
	}
	._lc:not(._ap):not(._ts)._lh13 .th {
		max-height: 2.6em;
	}
	._lc:not(._ap):not(._ts)._lh14 .th {
		max-height: 2.8em;
	}
	._lc:not(._ap):not(._ts)._lh15 .th {
		max-height: 3em;
	}
	._lc:not(._ap):not(._ts) .td {
		-webkit-line-clamp: 1;
	}
	._lc:not(._ap):not(._ts)._lh10 .td {
		max-height: 1em;
	}
	._lc:not(._ap):not(._ts)._lh11 .td {
		max-height: 1.1em;
	}
	._lc:not(._ap):not(._ts)._lh12 .td {
		max-height: 1.2em;
	}
	._lc:not(._ap):not(._ts)._lh13 .td {
		max-height: 1.3em;
	}
	._lc:not(._ap):not(._ts)._lh14 .td {
		max-height: 1.4em;
	}
	._lc:not(._ap):not(._ts)._lh15 .td {
		max-height: 1.5em;
	}
}
@media (min-width: 460px) {
	._lc:not(._ap):not(._ts) .td {
		-webkit-line-clamp: 2;
	}
	._lc:not(._ap):not(._ts)._lh10 .td {
		max-height: 2em;
	}
	._lc:not(._ap):not(._ts)._lh11 .td {
		max-height: 2.2em;
	}
	._lc:not(._ap):not(._ts)._lh12 .td {
		max-height: 2.4em;
	}
	._lc:not(._ap):not(._ts)._lh13 .td {
		max-height: 2.6em;
	}
	._lc:not(._ap):not(._ts)._lh14 .td {
		max-height: 2.8em;
	}
	._lc:not(._ap):not(._ts)._lh15 .td {
		max-height: 3em;
	}
}
._lc._ap:not(._ts) .th {
	-webkit-line-clamp: 3;
}
._lc._ap:not(._ts)._lh10 .th {
	max-height: 3em;
}
._lc._ap:not(._ts)._lh11 .th {
	max-height: 3.3em;
}
._lc._ap:not(._ts)._lh12 .th {
	max-height: 3.6em;
}
._lc._ap:not(._ts)._lh13 .th {
	max-height: 3.9em;
}
._lc._ap:not(._ts)._lh14 .th {
	max-height: 4.2em;
}
._lc._ap:not(._ts)._lh15 .th {
	max-height: 4.5em;
}
._lc._ap:not(._ts) .td {
	-webkit-line-clamp: 2;
}
._lc._ap:not(._ts)._lh10 .td {
	max-height: 2em;
}
._lc._ap:not(._ts)._lh11 .td {
	max-height: 2.2em;
}
._lc._ap:not(._ts)._lh12 .td {
	max-height: 2.4em;
}
._lc._ap:not(._ts)._lh13 .td {
	max-height: 2.6em;
}
._lc._ap:not(._ts)._lh14 .td {
	max-height: 2.8em;
}
._lc._ap:not(._ts)._lh15 .td {
	max-height: 3em;
}
@media (min-width: 360px) {
	._lc._ap:not(._ts) .th {
		-webkit-line-clamp: 2;
	}
	._lc._ap:not(._ts)._lh10 .th {
		max-height: 2em;
	}
	._lc._ap:not(._ts)._lh11 .th {
		max-height: 2.2em;
	}
	._lc._ap:not(._ts)._lh12 .th {
		max-height: 2.4em;
	}
	._lc._ap:not(._ts)._lh13 .th {
		max-height: 2.6em;
	}
	._lc._ap:not(._ts)._lh14 .th {
		max-height: 2.8em;
	}
	._lc._ap:not(._ts)._lh15 .th {
		max-height: 3em;
	}
	._lc._ap:not(._ts) .td {
		-webkit-line-clamp: 3;
	}
	._lc._ap:not(._ts)._lh10 .td {
		max-height: 3em;
	}
	._lc._ap:not(._ts)._lh11 .td {
		max-height: 3.3em;
	}
	._lc._ap:not(._ts)._lh12 .td {
		max-height: 3.6em;
	}
	._lc._ap:not(._ts)._lh13 .td {
		max-height: 3.9em;
	}
	._lc._ap:not(._ts)._lh14 .td {
		max-height: 4.2em;
	}
	._lc._ap:not(._ts)._lh15 .td {
		max-height: 4.5em;
	}
}
@media (min-width: 460px) {
	._lc._ap:not(._ts) .td {
		-webkit-line-clamp: 4;
	}
	._lc._ap:not(._ts)._lh10 .td {
		max-height: 4em;
	}
	._lc._ap:not(._ts)._lh11 .td {
		max-height: 4.4em;
	}
	._lc._ap:not(._ts)._lh12 .td {
		max-height: 4.8em;
	}
	._lc._ap:not(._ts)._lh13 .td {
		max-height: 5.2em;
	}
	._lc._ap:not(._ts)._lh14 .td {
		max-height: 5.6em;
	}
	._lc._ap:not(._ts)._lh15 .td {
		max-height: 6em;
	}
}
._lc._ts .th {
	-webkit-line-clamp: 1;
}
._lc._ts._lh10 .th {
	max-height: 1em;
}
._lc._ts._lh11 .th {
	max-height: 1.1em;
}
._lc._ts._lh12 .th {
	max-height: 1.2em;
}
._lc._ts._lh13 .th {
	max-height: 1.3em;
}
._lc._ts._lh14 .th {
	max-height: 1.4em;
}
._lc._ts._lh15 .th {
	max-height: 1.5em;
}
._lc._ts .td {
	-webkit-line-clamp: 2;
}
._lc._ts._lh10 .td {
	max-height: 2em;
}
._lc._ts._lh11 .td {
	max-height: 2.2em;
}
._lc._ts._lh12 .td {
	max-height: 2.4em;
}
._lc._ts._lh13 .td {
	max-height: 2.6em;
}
._lc._ts._lh14 .td {
	max-height: 2.8em;
}
._lc._ts._lh15 .td {
	max-height: 3em;
}
@media (min-width: 460px) {
	._lc._ts .th {
		-webkit-line-clamp: 1;
	}
	._lc._ts._lh10 .th {
		max-height: 1em;
	}
	._lc._ts._lh11 .th {
		max-height: 1.1em;
	}
	._lc._ts._lh12 .th {
		max-height: 1.2em;
	}
	._lc._ts._lh13 .th {
		max-height: 1.3em;
	}
	._lc._ts._lh14 .th {
		max-height: 1.4em;
	}
	._lc._ts._lh15 .th {
		max-height: 1.5em;
	}
	._lc._ts .td {
		-webkit-line-clamp: 3;
	}
	._lc._ts._lh10 .td {
		max-height: 3em;
	}
	._lc._ts._lh11 .td {
		max-height: 3.3em;
	}
	._lc._ts._lh12 .td {
		max-height: 3.6em;
	}
	._lc._ts._lh13 .td {
		max-height: 3.9em;
	}
	._lc._ts._lh14 .td {
		max-height: 4.2em;
	}
	._lc._ts._lh15 .td {
		max-height: 4.5em;
	}
}
@media (min-width: 460px) {
	._lc._xf:not(._xd)._ts .td {
		-webkit-line-clamp: 2;
	}
	._lc._xf:not(._xd)._ts._lh10 .td {
		max-height: 2em;
	}
	._lc._xf:not(._xd)._ts._lh11 .td {
		max-height: 2.2em;
	}
	._lc._xf:not(._xd)._ts._lh12 .td {
		max-height: 2.4em;
	}
	._lc._xf:not(._xd)._ts._lh13 .td {
		max-height: 2.6em;
	}
	._lc._xf:not(._xd)._ts._lh14 .td {
		max-height: 2.8em;
	}
	._lc._xf:not(._xd)._ts._lh15 .td {
		max-height: 3em;
	}
}
@media (min-width: 460px) {
	._lc._xf:not(._xd)._tl .td,
	._lc._xf:not(._xd)._tm .td {
		-webkit-line-clamp: 1;
	}
	._lc._xf:not(._xd)._tl._lh10 .td,
	._lc._xf:not(._xd)._tm .td {
		max-height: 1em;
	}
	._lc._xf:not(._xd)._tl._lh11 .td,
	._lc._xf:not(._xd)._tm .td {
		max-height: 1.1em;
	}
	._lc._xf:not(._xd)._tl._lh12 .td,
	._lc._xf:not(._xd)._tm .td {
		max-height: 1.2em;
	}
	._lc._xf:not(._xd)._tl._lh13 .td,
	._lc._xf:not(._xd)._tm .td {
		max-height: 1.3em;
	}
	._lc._xf:not(._xd)._tl._lh14 .td,
	._lc._xf:not(._xd)._tm .td {
		max-height: 1.4em;
	}
	._lc._xf:not(._xd)._tl._lh15 .td,
	._lc._xf:not(._xd)._tm .td {
		max-height: 1.5em;
	}
}
.t {
	-webkit-hyphens: auto;
	-moz-hyphens: auto;
	-ms-hyphens: auto;
	hyphens: auto;
}
.td,
.th {
	overflow: hidden;
	text-overflow: ellipsis;
	display: block;
}
@supports (display: -webkit-box) {
	.td,
	.th {
		display: -webkit-box;
		-webkit-box-orient: vertical;
	}
}
.td {
	vertical-align: inherit;
}
.tf,
.th {
	margin-bottom: 0.5em;
}
.td {
	margin-bottom: 0.6em;
}
._od .td:last-child,
._od .tf:last-child,
._od .th:last-child {
	margin-bottom: 0 !important;
}
._or .td {
	margin-bottom: 0 !important;
}
.tf {
	display: -ms-flexbox;
	display: flex;
	-ms-flex-align: center;
	align-items: center;
}
.tc {
	-ms-flex: 1;
	flex: 1;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}
.tim {
	display: block;
	min-width: 16px;
	min-height: 16px;
	width: 1em;
	height: 1em;
	margin-right: 6px;
}
._rtl .tim {
	margin-left: 6px;
	margin-right: 0;
}
.tx {
	opacity: 0.3;
	margin: 0 0.25em;
}
.tx:last-child {
	display: none !important;
}
._hd .td,
._hf .tf {
	display: none;
}
._hw .ti,
._hw .tw,
._hw .tw + .tx {
	display: none;
}
._hm .tm,
._hm .tw + .tx {
	display: none;
}
._hwi .ti {
	display: none;
}
._hwt .tw,
._hwt .tw + .tx {
	display: none;
}
._hmt .tmt,
._hmt .tmt + .tx {
	display: none;
}
._hmd .tm .tx,
._hmd .tmd {
	display: none;
}
._od._hf .td {
	margin-bottom: 0 !important;
}
._od._hd._hf .th,
._or._hd .th {
	margin-bottom: 0 !important;
}
@media (min-width: 460px) {
	.td {
		margin-bottom: 0.7em;
	}
}
._ffsa {
	font-family: -apple-system, system-ui, BlinkMacSystemFont, 'Segoe UI',
		Roboto, 'Helvetica Neue', Arial, sans-serif;
}
._ffse {
	font-family: Georgia, 'Times New Roman', Times, serif;
}
._ffmo {
	font-family: Menlo, Monaco, Consolas, 'Liberation Mono', 'Courier New',
		monospace;
}
._ffco {
	font-family: 'Comic Sans MS', 'Comic Sans', cursive;
}
._fwn {
	font-weight: 400;
}
._fwb {
	font-weight: 700;
}
._fsi {
	font-style: italic;
}
._fsn {
	font-style: normal;
}
._ttn {
	text-transform: none;
}
._ttu {
	text-transform: uppercase;
	letter-spacing: 0.025em;
}
._lh10 {
	line-height: 1;
}
._lh11 {
	line-height: 1.1;
}
._lh12 {
	line-height: 1.2;
}
._lh13 {
	line-height: 1.3;
}
._lh14 {
	line-height: 1.4;
}
._lh15 {
	line-height: 1.5;
}
._f3m {
	font-size: 11px;
}
._f0,
._f1m,
._f2m,
._f3m {
	font-size: 12px;
}
._f1p,
._f2p {
	font-size: 13px;
}
._f3p {
	font-size: 14px;
}
._f4p {
	font-size: 16px;
}
@media (min-width: 360px) {
	._f0 {
		font-size: 13px;
	}
	._f1p {
		font-size: 14px;
	}
	._f2p {
		font-size: 15px;
	}
	._f3p {
		font-size: 16px;
	}
	._f4p {
		font-size: 18px;
	}
}
@media (min-width: 460px) {
	._f1m {
		font-size: 13px;
	}
	._f0 {
		font-size: 14px;
	}
	._f1p {
		font-size: 15px;
	}
	._f2p {
		font-size: 16px;
	}
	._f3p {
		font-size: 18px;
	}
	._f4p {
		font-size: 21px;
	}
}
@media (min-width: 600px) {
	._f3m {
		font-size: 12px;
	}
	._f2m {
		font-size: 13px;
	}
	._f1m {
		font-size: 14px;
	}
	._f0 {
		font-size: 15px;
	}
	._f1p {
		font-size: 17px;
	}
	._f2p {
		font-size: 18px;
	}
	._f3p {
		font-size: 21px;
	}
	._f4p {
		font-size: 24px;
	}
}
.e {
	overflow: hidden;
	position: relative;
	width: 100%;
}
.e ._ls {
	height: 0;
	padding-bottom: 56.25%;
}
@supports (-moz-appearance: meterbar) and (all: initial) {
	._lc .e {
		-ms-flex: 1;
		flex: 1;
	}
}
._lc:not(._ap) .e {
	height: 100%;
	padding-bottom: 0;
}
.em {
	position: absolute;
	width: 100%;
	height: 100%;
}
.c,
.co {
	position: absolute;
	width: 100%;
	height: 100%;
}
.c {
	display: block;
	width: 100%;
	height: 100%;
	background: no-repeat center;
	background-size: cover;
}
.c {
	z-index: 20;
}
.co {
	z-index: 30;
}
.pr {
	position: absolute;
	width: 100%;
	height: 100%;
	z-index: 10;
}
.pr > video {
	width: 100%;
	height: 100%;
}
.pr .plyr {
	height: 100%;
}
.pv {
	display: block;
	width: 100%;
	height: 100%;
}
.w {
	background-color: inherit;
}
.t {
	line-height: 1.4;
	color: inherit;
}
.th {
	color: inherit;
}
.tf {
	color: #999;
}
.tw {
	color: #999;
}
</style>
  <div class="w __if _lc _sm _od _alsd _alcd _lh14 _xm _xi _ts _dm">
    <div class="wf">
      <div class="wc">
        <div class="e">
          <div class="em">
            <a href="https://github.com/spring-projects/spring-framework/wiki/Upgrading-to-Spring-Framework-6.x" target="_blank" rel="noopener" data-do-not-bind-click="" class="c" style="
                background-image: url('https://github.com/fluidicon.png');
              "></a>
          </div>
        </div>
      </div>
      <div class="wt">
        <div class="t _f0 _ffsa _fsn _fwn">
          <div class="th _f1p _fsn _fwb">
            <a href="https://github.com/spring-projects/spring-framework/wiki/Upgrading-to-Spring-Framework-6.x" target="_blank" rel="noopener" class="thl">Upgrading to Spring Framework 6.x</a>
          </div>
          <div class="td">Spring Framework. Contribute to spring-projects/spring-framework development by creating an account on GitHub.</div>
          <div class="tf _f1m">
            <div class="tc">
              <a href="https://github.com/spring-projects/spring-framework/wiki/Upgrading-to-Spring-Framework-6.x" target="_blank" rel="noopener" class="tw _f1m"><span class="twt">https://github.com/spring-projects/spring-framework/wiki/Upgrading-to-Spring-Framework-6.x</span><span class="twd">https://github.com/spring-projects/spring-framework/wiki/Upgrading-to-Spring-Framework-6.x</span></a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

*This page provides guidance on upgrading to Spring Framework 6.x.*

## Upgrading to Version 6.2

### Baseline upgrades

Spring Framework 6.2 raises its minimum requirements with the following libraries:

* For GraalVM native image support only, Hibernate 6.5+ is now required.

### Removed APIs

Several deprecated classes, constructors, and methods have been removed across the code base. See [30608](https://github.com/spring-projects/spring-framework/issues/30608) and [31492](https://github.com/spring-projects/spring-framework/issues/31492).

### Autowiring Algorithm

6.2 comes with a slightly revised autowiring algorithm where among a set of candidate beans that match by type, parameter name matches and `@Qualifier("...")` matches (against the target bean name) overrule `@jakarta.annotation.Priority` ranking whereas they were previously checked the other way around. That said, since we do not recommend mixing and matching those qualification mechanisms and generally do not recommend `@Priority` for identifying single candidates (rather just for ranking multiple candidates in an injected collection), we do not expect common regressions here. Note that `@Primary` beans always come first (and as a side note, 6.2 introduces the notion of `@Fallback` beans as well).

6.2 also comes with deeper generic type matching. If an injection point that previously matched does not match anymore, double-check your generic signatures at that injection point (e.g. your constructor argument) and for the bean type on the bean definition (e.g. the return type of your `@Bean` method). Spring is effectively less lenient in accepting fallback matches now, insisting on the resolvable part of the type signature to match even if the remaining part is leniently accepted with unresolvable type variables or wildcards.

### Conditional @ComponentScan

Component scanning happens early in the `BeanFactory` initialization and, as such, is not suitable to be guarded by a condition that is evaluated late. We now fail hard if you use `@ComponentScan` with a `REGISTER_BEAN` condition (such as Spring Boot's `@ConditionalOnBean`).

### Bean Definition Overriding

We've made it clearer that bean definition overriding is discouraged in production code, and the container now logs each override at `INFO` level. While not recommended, you can silence those logs by setting the `allowBeanDefinitionOverriding` flag to `true` on the bean factory or application context before it is refreshed.

### JMS DefaultMessageListenerContainer

The JMS `DefaultMessageListenerContainer` comes with revised `idleReceivesPerTaskLimit` semantics when using its default executor: Core threads always stay alive now, with only surplus consumers (between `concurrentConsumers` and `maxConcurrentConsumers`) timing out after the specified number of idle receives. Only in combination with a `maxMessagesPerTask` does `idleReceivesPerTaskLimit` have an effect on core consumers as well, as inferred for an external thread pool for dynamic rescheduling of all consumer tasks.

### HtmlUnit Support

Support for HtmlUnit has moved to a new major release that requires some changes when upgrading, see [Migrating from HtmlUnit 2.x.x to HtmlUnit 3.x.x](https://htmlunit.sourceforge.io/migration.html) for additional details.

If you are using HtmlUnit with Selenium, please note that the coordinates of the driver have changed, and the version now matches the Selenium version: `org.seleniumhq.selenium:htmlunit3-driver:X.Y.Z`, where `X.Y.Z` is your Selenium version.

### WebJars Support

`org.webjars:webjars-locator-core` support implemented in `WebJarsResourceResolver` is deprecated due to efficiency issues as of Spring Framework 6.2 and is superseded by `org.webjars:webjars-locator-lite` support implemented in `LiteWebJarsResourceResolver`.

## Upgrading to Version 6.1

### Baseline upgrades

Spring Framework 6.1 raises its minimum requirements with the following libraries:

* SnakeYAML 2.0
* Jackson 2.14
* Kotlin Coroutines 1.7
* Kotlin Serialization 1.5

### Removed APIs

Several deprecated classes, constructors, and methods have been removed across the code base. See [29449](https://github.com/spring-projects/spring-framework/issues/29449) and [30604](https://github.com/spring-projects/spring-framework/issues/30604).

RPC-style remoting that has been officially and/or effectively deprecated for several years has been removed. This impacts Hessian, HTTP Invoker, JMS Invoker, and JAX-WS support, see [27422](https://github.com/spring-projects/spring-framework/issues/27422).

EJB access has also been removed as part of this effort. If you need to lookup an EJB, use JNDI directly via `JndiObjectFactoryBean` or `[jee:jndi-lookup](jee:jndi-lookup)`.

### Parameter Name Retention

`LocalVariableTableParameterNameDiscoverer` has been removed in 6.1. Consequently, code within the Spring Framework and Spring portfolio frameworks no longer attempts to deduce parameter names by parsing bytecode. If you experience issues with dependency injection, property binding, SpEL expressions, or other use cases that depend on the names of parameters, you should compile your Java sources with the common Java 8+ `-parameters` flag for parameter name retention (instead of relying on the `-debug` compiler flag) in order to be compatible with `StandardReflectionParameterNameDiscoverer`. The Groovy compiler also supports a `-parameters` flag for the same purpose. With the Kotlin compiler, use the `-java-parameters` flag.

Maven users need to configure the `maven-compiler-plugin` for Java source code:

```
<plugin>
<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-compiler-plugin</artifactId>
<configuration>
<parameters>true</parameters>
</configuration>
</plugin>
```

Gradle users need to configure the `JavaCompile` task for Java source code, either with the Kotlin DSL:

```kotlin
tasks.withType<JavaCompile>() {
options.compilerArgs.add("-parameters")
}
```

Or the Groovy DSL:

```groovy
tasks.withType(JavaCompile).configureEach {
options.compilerArgs.add("-parameters")
}
```

Similarly, Gradle users need to configure the `GroovyCompile` task for Groovy source code, either with the Kotlin DSL:

```kotlin
tasks.withType<GroovyCompile>() {
groovyOptions.parameters = true
}
```

Or the Groovy DSL:

```groovy
tasks.withType(GroovyCompile).configureEach {
groovyOptions.parameters = true
}
```

Sometimes it is also necessary to manually configure your IDE.

In IntelliJ IDEA, open `Settings` and add `-parameters` to the following field.

* Build, Execution, Deployment → Compiler → Java Compiler → Additional command line parameters

In Eclipse IDE, open `Preferences` and activate the following checkbox.

* Java → Compiler → Store information about method parameters (usable via reflection)

### Core Container

Aligned with the deprecation of `java.net.URL` constructors in JDK 20, `URL` resolution is now consistently performed via `URI`, including the handling of relative paths. This includes behavioral changes for uncommon cases such as when specifying a full URL as a relative path. See [29481](https://github.com/spring-projects/spring-framework/issues/29481) and [28522](https://github.com/spring-projects/spring-framework/issues/28522).

`AutowireCapableBeanFactory.createBean(Class, int, boolean)` is deprecated now, in favor of the convention-based `createBean(Class)`. The latter is also consistently used internally in 6.1 – for example, in `SpringBeanJobFactory` for Quartz and `SpringBeanContainer` for Hibernate.

Array-to-collection conversion prefers a `List` result rather than a `Set` for a declared target type of `Collection`.

`ThreadPoolTaskExecutor` and `ThreadPoolTaskScheduler` enter a graceful shutdown phase when the application context starts to close. As a consequence, further task submissions are not accepted during stop or destroy callbacks in other components anymore. If the latter is necessary, switch the executor/scheduler's `acceptTasksAfterContextClose` flag to `true`, at the expense of a longer shutdown phase.

Message resolution through the `ApplicationContext` (accessing its internal `MessageSource`) is only allowed while the context is still active. After context close, `getMessage` attempts will throw an `IllegalStateException` now.

When building a native image, the verbose logging about pre-computed fields has been removed by default, and can be restored by passing `-Dspring.native.precompute.log=verbose` as a `native-image` compiler build argument to display related detailed logs.

### Caching

Spring's declarative caching infrastructure detects reactive method signatures, e.g. returning a Reactor `Mono` or `Flux`, and specifically processes such methods for asynchronous caching of their produced values rather than trying to cache the returned Reactive Streams `Publisher` instances themselves. This requires support in the target cache provider, e.g. with `CaffeineCacheManager` being set to `setAsyncCacheMode(true)`. For existing applications which rely on synchronous caching of custom `Mono.cache()`/`Flux.cache()` results, we recommend revising this towards 6.1-style caching of produced values; if such a revision is not immediately possible/desirable, you may set the system property "spring.cache.reactivestreams.ignore=true" (or put a similar entry into a `spring.properties` file on the classpath).

### Data Access and Transactions

`@TransactionalEventListener` rejects invalid `@Transactional` usage on the same method: only allowed as `REQUIRES_NEW` (possibly in combination with `@Async`).

JPA bootstrapping now fails in case of an incomplete Hibernate Validator setup (e.g. without an EL provider), making such a scenario easier to debug.

Since `JpaTransactionManager` with `HibernateJpaDialect` translates commit/rollback exceptions to `DataAccessException` subclasses wherever possible, a Hibernate transaction exception formerly propagated as a generic `JpaSystemException` may show up as e.g. `CannotAcquireLockException` now. For a non-translatable fallback exception, `JpaSystemException` will be consistently thrown for commit/rollback now, instead of the former `TransactionSystemException` propagated from rollback.

### Web Applications

Spring MVC and WebFlux now have built-in method validation support for controller method parameters with `@Constraint` annotations. To be in effect, you need to 1) opt out of AOP-based method validation by removing `@Validated` at the controller class level, 2) ensure `mvcValidator` or `webFluxValidator` beans are of type `jakarta.validation.Validator` (for example, `LocalValidatorFactoryBean`), and 3) have constraint annotations directly on method parameters. Where method validation is required (i.e. constraint annotations are present), model attribute and request body arguments with `@Valid` are also validated at the method level, and in that case no longer validated at the argument resolver level, thereby avoiding double validation. `BindingResult` arguments are still respected, but if not present or if method validation fails on other parameters, then a `MethodValidationException` raised. That's not handled yet in 6.1 M1, but will be in M2 with [30644](https://github.com/spring-projects/spring-framework/issues/30644). See [29825](https://github.com/spring-projects/spring-framework/issues/29825) for more details on the support in M1, and also the umbrella issue [30645](https://github.com/spring-projects/spring-framework/issues/30645) for all other related tasks and for providing feedback.

The format for `MethodArgumentNotValidException` and `WebExchangeBindException` message arguments has changed. Errors are now joined with `", and "`, without single quotes and brackets. Field errors are resolved through the `MessageSource` with nothing further such as the field name added. This gives applications full control over the error format by customizing individual error codes. See [30198](https://github.com/spring-projects/spring-framework/issues/30198) and also planned documentation improvement [30653](https://github.com/spring-projects/spring-framework/issues/30653).

The default order of mappings has been refined to be more consistent by changing `RouterFunctionMapping` order from `3` to `-1` in Spring MVC. That means `RouterFunctionMapping` is now always ordered before `RequestMappingHandlerMapping` in both Spring MVC and Spring WebFlux. See [30278](https://github.com/spring-projects/spring-framework/issues/30278) for more details.

The `throwExceptionIfNoHandlerFound` property of `DispatcherHandler` is now set to `true` by default and is deprecated. The resulting exception is handled by default as a 404 error so it should result in the same outcome. Likewise, `ResourceHttpRequestHandler` now raises `NoResourceFoundException`, which is also handled by default as a 404, and should have the same outcome for most applications. See [29491](https://github.com/spring-projects/spring-framework/issues/29491).

`@RequestParam`, `@RequestHeader`, and other controller method argument annotations now use the defaultValue if the input is a non-empty String without text.

`ResponseBodyEmitter` now completes the response if the exception is not an `IOException`, see issue [30687](https://github.com/spring-projects/spring-framework/issues/30687).

Preflight checks are now executed at the start of the `HandlerInteceptor` chain and not at the end.

The [HTTP interface client](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface) no longer enforces a 5 second default timeout on methods with a blocking signature, instead relying on default timeout and configuration settings of the underlying HTTP client. See [30248](https://github.com/spring-projects/spring-framework/issues/30248).

The HTTP server Observability instrumentation in WebFlux was limited and was not properly observing errors. As a result, the WebFlux `ServerHttpObservationFilter` is now deprecated in favor of direct instrumentation on the `WebHttpHandlerBuilder`. See [30013](https://github.com/spring-projects/spring-framework/issues/30013).

`ReactorResourceFactory` class has been moved from the `org.springframework.http.client.reactive` package to the `org.springframework.http.client` one.

To reduce memory usage in `RestClient` and `RestTemplate`, most `ClientHttpRequestFactory` implementations no longer buffer request bodies before sending them to the server. As a result, for certain content types such as JSON, the contents size is no longer known, and a `Content-Length` header is no longer set. If you would like to buffer request bodies like before, simply wrap the `ClientHttpRequestFactory` you are using in a `BufferingClientHttpRequestFactory`.

Jackson [`ParameterNamesModule`](https://github.com/FasterXML/jackson-modules-java8/tree/2.17/parameter-names) is now part of the well-known modules automatically registered by `Jackson2ObjectMapperBuilder` when present in the classpath. This can introduce changes of behavior in JSON serialization/deserialization as mentioned in the module documentation linked above. In such case, additional `@JsonCreator` or `@JsonProperty("propertyName")` annotations may be required. If you prefer avoid enabling such module, it is possible to use `Jackson2ObjectMapperBuilder#modules` in order to disable automatic module registration.

`ReactorClientHttpConnector` now implements `SmartLifecycle` to provide lifecycle management capabilities. As a consequence, it now requires `spring-context` dependency.

### Messaging Applications

The [RSocket interface client](https://docs.spring.io/spring-framework/reference/rsocket.html#rsocket-interface) no longer enforces a 5 second default timeout on methods with a blocking signature, instead relying on default timeout and configuration settings of the RSocket client, and the underlying RSocket transport. See [30248](https://github.com/spring-projects/spring-framework/issues/30248).

In an effort to reduce the potential for security vulnerabilities in the Spring Expression Language (SpEL) to adversely affect Spring applications, the team has decided to disable support for evaluating SpEL expressions from untrusted sources by default. Within the core Spring Framework, this applies to the SpEL-based `selector` header support in WebSocket messaging, specifically in the `DefaultSubscriptionRegistry`. The `selector` header support will remain in place but will have to be explicitly enabled beginning with Spring Framework 6.1 (see [30550](https://github.com/spring-projects/spring-framework/issues/30550)). For example, a custom implementation of `WebSocketMessageBrokerConfigurer` can override the `configureMessageBroker()` method and configure the selector header name as follows: `registry.enableSimpleBroker().setSelectorHeaderName("selector");`.

## Upgrading to Version 6.0

### Core Container

The JSR-330 based `@Inject` annotation is to be found in `jakarta.inject` now. The corresponding JSR-250 based annotations `@PostConstruct` and `@PreDestroy` are to be found in `jakarta.annotation`. For the time being, Spring keeps detecting their `javax` equivalents as well, covering common use in pre-compiled binaries.

The core container performs basic bean property determination without `java.beans.Introspector` by default. For full backwards compatibility with 5.3.x in case of sophisticated JavaBeans usage, specify the following content in a `META-INF/spring.factories` file which enables 5.3-style full `java.beans.Introspector` usage: `org.springframework.beans.BeanInfoFactory=org.springframework.beans.ExtendedBeanInfoFactory`

When staying on 5.3.x for the time being, you may enforce forward compatibility with 6.0-style property determination (and better introspection performance!) through a custom `META-INF/spring.factories` file: `org.springframework.beans.BeanInfoFactory=org.springframework.beans.SimpleBeanInfoFactory`

`LocalVariableTableParameterNameDiscoverer` is deprecated now and logs a warning for each successful resolution attempt (it only kicks in when `StandardReflectionParameterNameDiscoverer` has not found names). Compile your Java sources with the common Java 8+ `-parameters` flag for parameter name retention (instead of relying on the `-debug` compiler flag) in order to avoid that warning, or report it to the maintainers of the affected code. With the Kotlin compiler, we recommend the `-java-parameters` flag for completeness.

`LocalValidatorFactoryBean` relies on standard parameter name resolution in Bean Validation 3.0 now, just configuring additional Kotlin reflection if Kotlin is present. If you refer to parameter names in your Bean Validation setup, make sure to compile your Java sources with the Java 8+ `-parameters` flag.

`ListenableFuture` has been deprecated in favor of `CompletableFuture`. See [27780](https://github.com/spring-projects/spring-framework/issues/27780).

Methods annotated with `@Async` must return either `Future` or `void`. This has long been documented but is now also actively checked and enforced, with an exception thrown for any other return type. See [27734](https://github.com/spring-projects/spring-framework/issues/27734).

`SimpleEvaluationContext` disables array allocations now, aligned with regular constructor resolution.

### Caching

The `org.springframework.cache.ehcache` package has been removed as it was providing support for ehcache 2.x - with this version, `net.sf.ehcache` is using JavaEE APIs and [is about to be End Of Life&apos;d](https://github.com/ehcache/ehcache2). Ehcache3 is the direct replacement. You should revisit your dependency management to use `org.ehcache:ehcache` (with the `jakarta` classifier) instead and look [into the official migration guide or reach out to the ehcache community for assistance](https://www.ehcache.org/documentation/3.10/migration-guide.html). We did not replace `org.springframework.cache.ehcache` with an updated version, as using ehcache through the JCache API or its new native API is preferred.

### Data Access and Transactions

Due to the Jakarta EE migration, make sure to upgrade to Hibernate ORM 5.6.x with the `hibernate-core-jakarta` artifact, alongside switching your `javax.persistence` imports to `jakarta.persistence` (Jakarta EE 9). Alternatively, consider migrating to Hibernate ORM 6.1 right away (exclusively based on `jakarta.persistence`, compatible with EE 9 as well as EE 10) which is the Hibernate version that Spring Boot 3.0 comes with.

The corresponding Hibernate Validator generation is 7.0.x, based on `jakarta.validation` (Jakarta EE 9). You may also choose to upgrade to Hibernate Validator 8.0 right away (aligned with Jakarta EE 10).

For EclipseLink as the persistence provider of choice, the reference version is 3.0.x (Jakarta EE 9), with EclipseLink 4.0 as the most recent supported version (Jakarta EE 10).

Spring's default JDBC exception translator is the JDBC 4 based `SQLExceptionSubclassTranslator` now, detecting JDBC driver subclasses as well as common SQL state indications (without database product name resolution at runtime). As of 6.0.3, this includes a common SQL state check for `DuplicateKeyException`, addressing a long-standing difference between SQL state mappings and legacy default error code mappings.

`CannotSerializeTransactionException` and `DeadlockLoserDataAccessException` are deprecated as of 6.0.3 due to their inconsistent JDBC semantics, in favor of the `PessimisticLockingFailureException` base class and consistent semantics of its common `CannotAcquireLockException` subclass (aligned with JPA/Hibernate) in all default exception translation scenarios.

For full backwards compatibility with database-specific error codes, consider re-enabling the legacy `SQLErrorCodeSQLExceptionTranslator`. This translator kicks in for user-provided `sql-error-codes.xml` files. It can simply pick up Spring's legacy default error code mappings as well when triggered by an empty user-provided file in the root of the classpath.

### Web Applications

Due to the Jakarta EE migration, make sure to upgrade to Tomcat 10, Jetty 11, or Undertow 2.2.19 with the `undertow-servlet-jakarta` artifact, alongside switching your `javax.servlet` imports to `jakarta.servlet` (Jakarta EE 9). For the latest server generations, consider Tomcat 10.1 and Undertow 2.3 (Jakarta EE 10).

Several outdated Servlet-based integrations have been dropped: e.g. Apache Commons FileUpload (`org.springframework.web.multipart.commons.CommonsMultipartResolver`), and Apache Tiles as well as FreeMarker JSP support in the corresponding `org.springframework.web.servlet.view` subpackages. We recommend `org.springframework.web.multipart.support.StandardServletMultipartResolver` for multipart file uploads and regular FreeMarker template views if needed, and a general focus on REST-oriented web architectures.

Spring MVC and Spring WebFlux no longer detect controllers based solely on a type-level `@RequestMapping` annotation. That means interface-based AOP proxying for web controllers may no longer work. Please, enable class-based proxying for such controllers; otherwise the interface must also be annotated with `@Controller`. See [22154](https://github.com/spring-projects/spring-framework/issues/22154).

`HttpMethod` is now a class and no longer an enum. Though the public API has been maintained, some migration might be necessary (i.e. change from `EnumSet<HttpMethod>` to `Set<HttpMethod>`, use `if  else` instead of `switch`). For the rationale behind this decision, see [27697](https://github.com/spring-projects/spring-framework/issues/27697).

The Kotlin extension function to `WebTestClient.ResponseSpec::expectBody` now returns the Java `BodySpec` type and no longer uses the workaround type `KotlinBodySpec`. Spring 6.0 uses Kotlin 1.6, which fixed the bug that needed this workaround ([KT-5464](https://youtrack.jetbrains.com/issue/KT-5464)). This means that `consumeWith` is no longer available.

`RestTemplate`, or rather the `HttpComponentsClientHttpRequestFactory`, now requires Apache HttpClient 5.

The Spring-provided Servlet mocks (`MockHttpServletRequest`, `MockHttpSession`) require Servlet 6.0 now, due to a breaking change between the Servlet 5.0 and 6.0 API jars. They can be used for testing Servlet 5.0 based code but need to run against the Servlet 6.0 API (or newer) on the test classpath. Note that your production code may still compile against Servlet 5.0 and get integration-tested with Servlet 5.0 based containers; just mock-based tests need to run against the Servlet 6.0 API jar.

`SourceHttpMessageConverter` is not configured by default anymore in Spring MVC and `RestTemplate`. As a consequence, Spring web applications using `javax.xml.transform.Source` now need to configure `SourceHttpMessageConverter` explicitly. Note that the order of converter registration is important, and `SourceHttpMessageConverter` should typically be registered before "catch-all" converters like `MappingJackson2HttpMessageConverter` for example.
