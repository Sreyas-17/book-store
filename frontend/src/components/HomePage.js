import React, { useEffect } from 'react';
import { useBooks } from '../hooks/useBooks';
import { useAuth } from '../contexts/AuthContext';
import BookCard from './BookCard';

const HomePage = () => {
  const { books = [], loading, error, fetchBooks } = useBooks();
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    if (!books || books.length === 0) {
      fetchBooks();
    }
  }, []);

  const safeBooks = Array.isArray(books) ? books : [];
  const featuredBooks = safeBooks.slice(0, 8);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Loading amazing books for you...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="text-6xl mb-4">📚</div>
          <h2 className="text-2xl font-bold text-gray-900 mb-2">Oops! Something went wrong</h2>
          <p className="text-gray-600 mb-6">{error}</p>
          <button onClick={() => window.location.reload()} className="btn btn-primary">
            Try Again
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen">
      {/* Hero Section */}
      <section className="bg-gradient-to-br from-blue-600 via-blue-700 to-purple-800 text-white py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
            <div>
              <h1 className="text-5xl lg:text-6xl font-bold mb-6">
                Discover Your Next
                <span className="block bg-gradient-to-r from-yellow-400 to-orange-500 bg-clip-text text-transparent">
                  Great Read
                </span>
              </h1>
              <p className="text-xl mb-8 text-blue-100">
                Explore thousands of books from bestselling authors, discover new genres, 
                and find your perfect literary companion.
              </p>
              <div className="flex flex-wrap gap-8 mb-8">
                <div className="text-center">
                  <div className="text-3xl font-bold text-yellow-300">{safeBooks.length}+</div>
                  <div className="text-blue-200">Books Available</div>
                </div>
                <div className="text-center">
                  <div className="text-3xl font-bold text-yellow-300">50+</div>
                  <div className="text-blue-200">Authors</div>
                </div>
                <div className="text-center">
                  <div className="text-3xl font-bold text-yellow-300">15+</div>
                  <div className="text-blue-200">Genres</div>
                </div>
              </div>
              {!isAuthenticated && (
                <div className="flex flex-wrap gap-4">
                  <a href="/register" className="bg-white text-blue-600 hover:bg-gray-100 px-8 py-3 rounded-lg font-semibold transition-colors duration-200 inline-block">
                    Start Reading Today
                  </a>
                  <a href="/login" className="border-2 border-white text-white hover:bg-white hover:text-blue-600 px-8 py-3 rounded-lg font-semibold transition-colors duration-200 inline-block">
                    Sign In
                  </a>
                </div>
              )}
            </div>
            <div className="relative">
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-4">
                  <div className="bg-white bg-opacity-10 backdrop-blur-sm rounded-lg p-6 text-center">
                    <div className="text-4xl mb-2">📖</div>
                    <div className="text-sm">Fiction</div>
                  </div>
                  <div className="bg-white bg-opacity-10 backdrop-blur-sm rounded-lg p-6 text-center">
                    <div className="text-4xl mb-2">🔬</div>
                    <div className="text-sm">Science</div>
                  </div>
                </div>
                <div className="space-y-4 mt-8">
                  <div className="bg-white bg-opacity-10 backdrop-blur-sm rounded-lg p-6 text-center">
                    <div className="text-4xl mb-2">🎭</div>
                    <div className="text-sm">Drama</div>
                  </div>
                  <div className="bg-white bg-opacity-10 backdrop-blur-sm rounded-lg p-6 text-center">
                    <div className="text-4xl mb-2">🚀</div>
                    <div className="text-sm">Sci-Fi</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-20 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-16">
            <h2 className="text-4xl font-bold text-gray-900 mb-4">Why Choose Our Bookstore?</h2>
            <p className="text-xl text-gray-600">Experience the best in online book shopping</p>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
            {[
              { icon: '🚚', title: 'Fast Delivery', desc: 'Get your books delivered within 2-3 business days' },
              { icon: '💰', title: 'Best Prices', desc: 'Competitive prices with regular discounts and offers' },
              { icon: '⭐', title: 'Quality Assured', desc: 'Carefully curated collection of high-quality books' },
              { icon: '🔒', title: 'Secure Payment', desc: 'Safe and secure payment options for your peace of mind' }
            ].map((feature, index) => (
              <div key={index} className="text-center p-6">
                <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
                  <span className="text-2xl">{feature.icon}</span>
                </div>
                <h3 className="text-xl font-semibold mb-2">{feature.title}</h3>
                <p className="text-gray-600">{feature.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Books Section */}
      <section className="py-20 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-4xl font-bold text-gray-900 mb-4">Featured Books</h2>
            <p className="text-xl text-gray-600">Handpicked selections from our extensive collection</p>
          </div>

          {safeBooks.length === 0 ? (
            <div className="text-center py-20">
              <div className="text-6xl mb-4">📚</div>
              <h3 className="text-2xl font-semibold text-gray-900 mb-2">No Books Available</h3>
              <p className="text-gray-600 mb-4">We're working hard to stock our shelves with amazing books.</p>
              <p className="text-gray-600">Check back soon for new arrivals!</p>
            </div>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
              {featuredBooks.map((book) => (
                <BookCard key={book.id} book={book} />
              ))}
            </div>
          )}

          {safeBooks.length > 8 && (
            <div className="text-center mt-12">
              <button className="bg-blue-600 hover:bg-blue-700 text-white px-8 py-3 rounded-lg font-semibold transition-colors duration-200">
                View All {safeBooks.length} Books
              </button>
            </div>
          )}
        </div>
      </section>

      {/* Newsletter Section */}
      <section className="py-20 bg-gray-900 text-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-2xl mx-auto">
            <h2 className="text-3xl font-bold mb-4">Stay Updated</h2>
            <p className="text-gray-300 mb-8">Get notified about new arrivals, special offers, and book recommendations</p>
            <form className="flex flex-col sm:flex-row gap-4 max-w-md mx-auto">
              <input 
                type="email" 
                placeholder="Enter your email address"
                className="flex-1 px-4 py-3 rounded-lg text-gray-900 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <button type="submit" className="bg-blue-600 hover:bg-blue-700 text-white px-8 py-3 rounded-lg font-semibold transition-colors duration-200">
                Subscribe
              </button>
            </form>
          </div>
        </div>
      </section>
    </div>
  );
};

export default HomePage;
