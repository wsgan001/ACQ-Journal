set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [1.4005285292E4	1.4783593774E4	1.4756739734E4	1.1464716054E4	1.4642411502E4];
y2 = [1.88077066	1.18947918	0.97299902	0.7413678181818183	0.4106826060606061];
y3 = [205.76418324	326.12487565	243.5644892	240.57571266	188.91780958];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('the core number of the vertex');
ylabel('time (ms)');

axis([0.5 5.5 0.4 1.5E4]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'5','10','15','20','25'});
leg=legend('rebuild','insert','delete', 2);
set(leg,'edgecolor','white');
set(gca,'ytick',[10^0,10^1,10^2,10^3,10^4])
set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);