import { Input, Select, Space } from 'antd';

const { Search } = Input;

const TIME_OPTIONS = [
  { value: '', label: 'All' },
  { value: 'today', label: 'Today' },
  { value: 'week', label: 'One week' },
  { value: 'month', label: 'One month' },
];

// Controlled filter bar. Parent owns the filter state.
export default function SearchFilters({
  keyword,
  city,
  language,
  timeRange,
  cities = [],
  languages = [],
  onKeywordChange,
  onCityChange,
  onLanguageChange,
  onTimeRangeChange,
  onSearch,
}) {
  const cityOptions = [
    { value: '', label: 'All' },
    ...cities.map((c) => ({ value: c.cityName, label: c.cityName })),
  ];
  const languageOptions = [
    { value: '', label: 'All' },
    ...languages.map((l) => ({ value: l.languageName, label: l.languageName })),
  ];

  return (
    <Space direction="vertical" size="middle" style={{ width: '100%' }}>
      <Search
        placeholder="Search events"
        enterButton="Search"
        value={keyword}
        onChange={(e) => onKeywordChange?.(e.target.value)}
        onSearch={onSearch}
        size='large'
        style={{ maxWidth: 480 }}
      />
      <Space size="large" wrap>
        <Space>
          <span>City:</span>
          <Select
            value={city ?? ''}
            options={cityOptions}
            onChange={onCityChange}
            style={{ width: 160 }}
          />
        </Space>
        <Space>
          <span>Language:</span>
          <Select
            value={language ?? ''}
            options={languageOptions}
            onChange={onLanguageChange}
            style={{ width: 160 }}
          />
        </Space>
        <Space>
          <span>Time:</span>
          <Select
            value={timeRange ?? ''}
            options={TIME_OPTIONS}
            onChange={onTimeRangeChange}
            style={{ width: 140 }}
          />
        </Space>
      </Space>
    </Space>
  );
}
