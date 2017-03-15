set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [13432.601710089999 13614.38845903	13543.553310390002	13842.78493123	13758.51194286];
y2 = [36336.67515819 40265.0880997 	39924.88111317	41101.83709054	38636.50744126];
y3 = [326.86276739 323.37266381	320.62223059	309.01563559	314.14727198];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('the length of query set in Q');
ylabel('time (ms)');

axis([0.5 5.5 100.0 100000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'2','3','4','5','6'});
leg=legend('basic-g-v2','basic-w-v2','MDec', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);