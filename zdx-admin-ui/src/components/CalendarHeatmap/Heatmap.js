

export class Heatmap {

	static  DEFAULT_RANGE_COLOR_LIGHT = [ '#ebedf0', '#dae2ef', '#c0ddf9', '#73b3f3', '#3886e1', '#17459e' ];
	static  DEFAULT_RANGE_COLOR_DARK  = [ '#1f1f22', '#1e334a', '#1d466c', '#1d5689', '#1d69ac', '#1B95D1' ];
	static  DEFAULT_LOCALE = {
		months: [ '一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '十一', '十二' ],
		days  : [ '日', '一', '二', '三', '四', '五', '六' ],
		on    : '发布于',
		less  : '少',
		more  : '多'
	};
	static DEFAULT_TOOLTIP_UNIT   = '篇文章';
	static  DAYS_IN_ONE_YEAR       = 365;
	static  DAYS_IN_WEEK           = 7;
	static  SQUARE_SIZE            = 10;

	startDate;
	endDate;
	max;

	 _values;
	 _firstFullWeekOfMonths;
	 _activities;
	 _calendar;

	constructor(endDate, values, max) {
	this.endDate   = this.parseDate(endDate);
	this.max       = max || Math.ceil((Math.max(...values.map(day => day.count)) / 5) * 4);
	this.startDate = this.shiftDate(endDate, -Heatmap.DAYS_IN_ONE_YEAR);
	this._values   = values;
}

set values(v) {
	this.max                    = Math.ceil((Math.max(...v.map(day => day.count)) / 5) * 4);
	this._values                = v;
	this._firstFullWeekOfMonths = undefined;
	this._calendar              = undefined;
	this._activities            = undefined;
}

get values() {
	return this._values;
}

get activities() {
	if (!this._activities) {
		this._activities = new Map();
		for (let i = 0, len = this.values.length; i < len; i++) {
			this._activities.set(this.keyDayParser(this.values[ i ].date), {
				count     : this.values[ i ].count,
				colorIndex: this.getColorIndex(this.values[ i ].count)
			});
		}
	}
	return this._activities;
}

get weekCount() {
	return this.getDaysCount() / Heatmap.DAYS_IN_WEEK;
}

get calendar() {
	if (!this._calendar) {
		let date       = this.shiftDate(this.startDate, -this.getCountEmptyDaysAtStart());
		date           = new Date(date.getFullYear(), date.getMonth(), date.getDate());
		this._calendar = new Array(this.weekCount);
		for (let i = 0, len = this._calendar.length; i < len; i++) {
			this._calendar[ i ] = new Array(Heatmap.DAYS_IN_WEEK);
			for (let j = 0; j < Heatmap.DAYS_IN_WEEK; j++) {
				const dayValues          = this.activities.get(this.keyDayParser(date));
				this._calendar[ i ][ j ] = {
					date      : new Date(date.valueOf()),
					count     : dayValues ? dayValues.count : undefined,
					colorIndex: dayValues ? dayValues.colorIndex : 0
				};
				date.setDate(date.getDate() + 1);
			}
		}
	}
	return this._calendar;
}

get firstFullWeekOfMonths() {
	if (!this._firstFullWeekOfMonths) {
		const cal                   = this.calendar;
		this._firstFullWeekOfMonths = [];
		for (let index = 1, len = cal.length; index < len; index++) {
			const lastWeek    = cal[ index - 1 ][ 0 ].date,
				currentWeek = cal[ index ][ 0 ].date;
			if (lastWeek.getFullYear() < currentWeek.getFullYear() || lastWeek.getMonth() < currentWeek.getMonth()) {
				this._firstFullWeekOfMonths.push({ value: currentWeek.getMonth(), index });
			}
		}
	}
	return this._firstFullWeekOfMonths;
}

getColorIndex(count) {
	if (count === null || count === undefined) {
		return 0;
	} else if (count <= 0) {
		return 1;
	} else if (count >= this.max) {
		return 5;
	} else {
		return (Math.ceil(((count * 100) / this.max) * (0.03))) + 1;
	}
}

getCountEmptyDaysAtStart() {
	return this.startDate.getDay();
}

getCountEmptyDaysAtEnd() {
	return (Heatmap.DAYS_IN_WEEK - 1) - this.endDate.getDay();
}

getDaysCount() {
	return Heatmap.DAYS_IN_ONE_YEAR + 1 + this.getCountEmptyDaysAtStart() + this.getCountEmptyDaysAtEnd();
}

 shiftDate(date, numDays) {
	const newDate = new Date(date);
	newDate.setDate(newDate.getDate() + numDays);
	return newDate;
}

 parseDate(entry) {
	return (entry instanceof Date) ? entry : (new Date(entry));
}

 keyDayParser(date) {
	const day = this.parseDate(date);
	return String(day.getFullYear()) + String(day.getMonth()).padStart(2, '0') + String(day.getDate()).padStart(2, '0');
}

}
