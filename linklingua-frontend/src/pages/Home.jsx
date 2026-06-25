import { useEffect, useState } from 'react';
import { Row, Col, Empty, Spin, Pagination, message } from 'antd';
import axios from 'axios';
import SearchFilters from '../components/SearchFilters';
import EventCard from '../components/EventCard';
import { pageEvents, getCities, getLanguages } from '../api';

const PAGE_SIZE = 9;

export default function Home() {
  // Draft filter inputs (what the user is currently editing).
  const [keyword, setKeyword] = useState('');
  const [city, setCity] = useState('');
  const [language, setLanguage] = useState('');
  const [timeRange, setTimeRange] = useState('');

  // Applied keyword: only changes when the user clicks "Search",
  // so typing doesn't trigger a request on every keystroke.
  const [appliedKeyword, setAppliedKeyword] = useState('');
  const [page, setPage] = useState(1);

  const [cities, setCities] = useState([]);
  const [languages, setLanguages] = useState([]);

  const [events, setEvents] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);

  // Load filter dropdown options once.
  useEffect(() => {
    const controller = new AbortController();
    getCities({ signal: controller.signal })
      .then((res) => setCities(res.data || []))
      .catch(() => {});
    getLanguages({ signal: controller.signal })
      .then((res) => setLanguages(res.data || []))
      .catch(() => {});
    return () => controller.abort();
  }, []);

  // Single source of truth for fetching events. It re-runs only when an
  // *applied* query parameter changes. The AbortController cancels any
  // in-flight request (rapid filter changes) and the throwaway request
  // that React StrictMode triggers in development, so no duplicates land.
  useEffect(() => {
    const controller = new AbortController();
    // eslint-disable-next-line react-hooks/set-state-in-effect
    setLoading(true);
    pageEvents(
      {
        page,
        pageSize: PAGE_SIZE,
        city: city || undefined,
        language: language || undefined,
        timeRange: timeRange || undefined,
        keyword: appliedKeyword || undefined,
      },
      { signal: controller.signal }
    )
      .then((res) => {
        const data = res.data || {};
        // Backend pagination shape may be { records/rows/list, total }.
        const records = data.records || data.rows || data.list || [];
        setEvents(records);
        setTotal(data.total ?? records.length);
      })
      .catch((err) => {
        if (axios.isCancel(err)) return;
        message.error('Failed to load events');
      })
      .finally(() => {
        if (!controller.signal.aborted) setLoading(false);
      });

    return () => controller.abort();
  }, [city, language, timeRange, appliedKeyword, page]);

  // Changing a filter always returns to the first page. Both state updates
  // are batched into one render, so the fetch effect runs exactly once.
  const handleCityChange = (value) => {
    setCity(value);
    setPage(1);
  };
  const handleLanguageChange = (value) => {
    setLanguage(value);
    setPage(1);
  };
  const handleTimeRangeChange = (value) => {
    setTimeRange(value);
    setPage(1);
  };
  const handleSearch = () => {
    setAppliedKeyword(keyword);
    setPage(1);
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'center', marginBottom: 32 }}>
        <div style={{ width: '100%', maxWidth: 700 }}>
          <SearchFilters
            keyword={keyword}
            city={city}
            language={language}
            timeRange={timeRange}
            cities={cities}
            languages={languages}
            onKeywordChange={setKeyword}
            onCityChange={handleCityChange}
            onLanguageChange={handleLanguageChange}
            onTimeRangeChange={handleTimeRangeChange}
            onSearch={handleSearch}
          />
        </div>
      </div>

      <Spin spinning={loading}>
        {events.length === 0 && !loading ? (
          <Empty description="No events found" />
        ) : (
          <Row gutter={[24, 24]}>
            {events.map((event) => (
              <Col key={event.id} xs={24} sm={12} md={8}>
                <EventCard event={event} />
              </Col>
            ))}
          </Row>
        )}
      </Spin>

      {total > PAGE_SIZE && (
        <div style={{ display: 'flex', justifyContent: 'center', marginTop: 32 }}>
          <Pagination
            current={page}
            pageSize={PAGE_SIZE}
            total={total}
            showSizeChanger={false}
            onChange={(p) => setPage(p)}
          />
        </div>
      )}
    </div>
  );
}
